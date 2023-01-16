package com.github.khoben.fill_blanks

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue.COMPLEX_UNIT_PX
import android.view.DragEvent
import android.view.Gravity
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatEditText
import kotlin.math.roundToInt

sealed class Chunk(open val text: String) {
    data class Text(override val text: String) : Chunk(text)
    data class Blank(override val text: String) : Chunk(text)
}

class FillBlanksView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextFlowLayout(context, attrs) {

    private val measurePaint: TextPaint = TextPaint()

    private val chunksEditViews = mutableListOf<EditText>()
    private var startTag: String = TAG_START_BLANK
    private var endTag: String = TAG_END_BLANK

    private var textSize: Int = DEFAULT_TEXT_SIZE

    @ColorInt
    private var textColor: Int = DEFAULT_TEXT_COLOR

    @ColorInt
    private var validColor: Int = DEFAULT_VALID_COLOR

    @ColorInt
    private var invalidColor: Int = DEFAULT_INVALID_COLOR

    private var draggable: Boolean = false

    init {
        context.obtainStyledAttributes(
            attrs, R.styleable.FillBlanksView, defStyleAttr, 0
        ).apply {
            try {
                draggable = getBoolean(R.styleable.FillBlanksView_draggable, false)

                startTag = getString(R.styleable.FillBlanksView_start_tag) ?: TAG_START_BLANK
                endTag = getString(R.styleable.FillBlanksView_end_tag) ?: TAG_END_BLANK

                textSize =
                    getDimensionPixelSize(R.styleable.FillBlanksView_font_size, DEFAULT_TEXT_SIZE)
                textColor = getColor(R.styleable.FillBlanksView_text_color, DEFAULT_TEXT_COLOR)
                validColor =
                    getColor(R.styleable.FillBlanksView_correct_answer_color, DEFAULT_VALID_COLOR)
                invalidColor = getColor(
                    R.styleable.FillBlanksView_incorrect_answer_color,
                    DEFAULT_INVALID_COLOR
                )
            } finally {
                recycle()
            }
        }
        measurePaint.textSize = textSize.toFloat()
    }

    fun updateText(text: String) {
        post {
            internalUpdateText(text)
        }
    }

    fun getUserAnswers(): List<String> {
        return chunksEditViews.map { it.text.toString() }
    }

    fun validate(data: List<State>) {
        require(data.size == chunksEditViews.size) {
            "Size mismatch data.size=${data.size} != chunksEditViews.size=${chunksEditViews.size}"
        }

        data.forEachIndexed { idx, state ->
            chunksEditViews[idx].lineColor(
                when (state) {
                    State.NONE -> textColor
                    State.VALID -> validColor
                    State.INVALID -> invalidColor
                }
            )
        }
        hideKeyboard()
    }

    private fun internalUpdateText(text: String) {
        removeAllViews()
        chunksEditViews.clear()

        val maxLineCount = (measuredWidth / measurePaint.measureText("m")).roundToInt()
        val textChunks = mutableListOf<Chunk>()

        var viewWidth = 0f

        var rawText = text
        while (rawText.contains(startTag)) {
            var chunk = rawText.substring(0, rawText.indexOf(startTag))

            textChunks.addAll(chunk.chunked(maxLineCount).map { Chunk.Text(it) })
            // cut text chunk from raw text
            rawText = rawText.replace(chunk, "")

            chunk = rawText.substring(
                rawText.indexOf(startTag),
                rawText.indexOf(endTag) + 1
            )
            textChunks.add(Chunk.Blank(chunk.replace(startTag, "").replace(endTag, "")))
            // cut blank chunk from raw text
            rawText = rawText.replace(chunk, "")
        }

        if (rawText.isNotEmpty()) {
            textChunks.addAll(rawText.chunked(maxLineCount).map { Chunk.Text(it) })
        }

        textChunks.forEach { chunk ->
            val chunkString = chunk.text
            var chunkWidth = measurePaint.measureText(chunkString)
            when (chunk) {
                is Chunk.Text -> {
                    if (viewWidth + chunkWidth < measuredWidth - DEFAULT_MARGIN) {
                        val textView = TextView(context)
                        textView.text = chunkString
                        textView.setPadding(0, 0, 0, 0)
                        textView.setTextSize(COMPLEX_UNIT_PX, textSize.toFloat())
                        textView.layoutParams =
                            MarginLayoutParams(chunkWidth.toInt(), LayoutParams.WRAP_CONTENT)
                        textView.setTextColor(textColor)
                        addView(textView)

                        viewWidth += chunkWidth
                    } else {
                        // cut from end
                        for (j in chunkString.length downTo 0) {
                            val chunkOfChunk = chunkString.substring(0, j)
                            chunkWidth = measurePaint.measureText(chunkOfChunk)
                            if (viewWidth + chunkWidth < measuredWidth - DEFAULT_MARGIN) {
                                val textView = TextView(context)
                                textView.text = chunkOfChunk
                                textView.setPadding(0, 0, 0, 0)
                                textView.setTextSize(COMPLEX_UNIT_PX, textSize.toFloat())
                                textView.layoutParams =
                                    MarginLayoutParams(
                                        chunkWidth.toInt(),
                                        LayoutParams.WRAP_CONTENT
                                    )
                                textView.setTextColor(textColor)
                                addView(textView)

                                chunkWidth =
                                    measurePaint.measureText(
                                        chunkString.substring(
                                            j,
                                            chunkString.length
                                        )
                                    )
                                val additionalTextView = TextView(context)
                                additionalTextView.text =
                                    chunkString.substring(j, chunkString.length)
                                additionalTextView.setPadding(0, 0, 0, 0)
                                additionalTextView.setTextSize(COMPLEX_UNIT_PX, textSize.toFloat())
                                additionalTextView.layoutParams =
                                    MarginLayoutParams(
                                        chunkWidth.toInt(),
                                        LayoutParams.WRAP_CONTENT
                                    )
                                additionalTextView.setTextColor(textColor)
                                addView(additionalTextView)

                                viewWidth = chunkWidth
                                break
                            }
                        }
                    }
                }
                is Chunk.Blank -> {

                    val editText = object : AppCompatEditText(context) {
                        override fun getDefaultEditable(): Boolean = !draggable
                    }
                    // move to start after edit
                    editText.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
                        if (!hasFocus) {
                            editText.setSelection(0)
                        }
                    }
                    editText.setOnDragListener { v, event ->
                        val view = (v as EditText)
                        when (event.action) {
                            DragEvent.ACTION_DRAG_STARTED -> {
                            }
                            DragEvent.ACTION_DROP -> {
                                val item = event.clipData.getItemAt(0)
                                val paste = item.text
                                with(view) {
                                    setText(paste.toString())
                                    setSelection(0)
                                }
                            }
                            DragEvent.ACTION_DRAG_ENDED, DragEvent.ACTION_DRAG_EXITED -> {
                                view.lineColor(textColor)
                            }
                            DragEvent.ACTION_DRAG_ENTERED -> {
                                // TODO: Entered color
                                view.lineColor(Color.GREEN)
                            }
                            else -> {}
                        }
                        true
                    }
                    if (draggable) {
                        editText.setOnClickListener { v ->
                            (v as EditText).text.clear()
                        }
                        editText.isFocusable = false
                        editText.isFocusableInTouchMode = false
                    }
                    editText.isSingleLine = true
                    editText.ellipsize = TextUtils.TruncateAt.END
                    editText.gravity = Gravity.CENTER
                    editText.setTextSize(COMPLEX_UNIT_PX, textSize.toFloat())
                    editText.setPadding(0, 0, 0, 6.dp)
                    editText.maxWidth = chunkWidth.toInt()
                    editText.layoutParams =
                        MarginLayoutParams(chunkWidth.toInt(), LayoutParams.WRAP_CONTENT)
                    editText.setTextColor(textColor)

                    chunksEditViews.add(editText)

                    addView(editText)

                    viewWidth = if (chunkWidth + viewWidth < measuredWidth - DEFAULT_MARGIN) {
                        viewWidth + chunkWidth
                    } else {
                        chunkWidth
                    }
                }
            }
        }
    }

    private fun EditText.lineColor(@ColorInt color: Int) {
        background.mutate().colorFilter =
            PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }

    companion object {
        private const val DEFAULT_MARGIN = 10
        private const val TAG_START_BLANK = "["
        private const val TAG_END_BLANK = "]"

        private val DEFAULT_TEXT_SIZE = 16.sp

        private const val DEFAULT_VALID_COLOR = Color.GREEN
        private const val DEFAULT_INVALID_COLOR = Color.RED
        private const val DEFAULT_TEXT_COLOR = Color.BLACK
    }
}