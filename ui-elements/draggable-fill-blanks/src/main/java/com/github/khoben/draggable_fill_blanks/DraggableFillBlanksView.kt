package com.github.khoben.draggable_fill_blanks

import android.content.ClipData
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.DragStartHelper
import androidx.core.view.ViewCompat
import com.github.khoben.draggable_fill_blanks.databinding.DraggableLayoutBinding
import com.github.khoben.fill_blanks.State

class DraggableFillBlanksView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val fillBlanksBinding = DraggableLayoutBinding.inflate(
        LayoutInflater.from(context), this
    )

    init {
        orientation = VERTICAL

        // TODO: Draggable placeholder
        listOf(fillBlanksBinding.draggable1, fillBlanksBinding.draggable2).forEach { draggable ->
            DragStartHelper(draggable) { view, _ ->
                val text = (view as TextView).text
                val dragClipData = ClipData.newPlainText("Text", text)
                val dragShadowBuilder = DragShadowBuilder(view)
                ViewCompat.startDragAndDrop(view, dragClipData, dragShadowBuilder, null, 0)
            }.attach()
        }
    }

    fun updateText(text: String) = fillBlanksBinding.editText.updateText(text)

    fun getUserAnswers(): List<String> = fillBlanksBinding.editText.getUserAnswers()

    fun validate(data: List<State>) = fillBlanksBinding.editText.validate(data)
}