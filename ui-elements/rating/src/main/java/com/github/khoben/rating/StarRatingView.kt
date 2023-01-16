package com.github.khoben.rating

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.FloatRange
import androidx.annotation.UiThread
import androidx.core.content.res.ResourcesCompat
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min


class StarRatingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val selectedIcon: Drawable
    private val notSelectedIcon: Drawable

    private var currentRating = 1f
    private var ratingStep = RATING_STEP
    private var maxRating = MAX_RATING
    private var gapSize = GAP_SIZE

    private var listener: OnRatingChangedListener = OnRatingChangedListener.NOOP

    init {
        context.obtainStyledAttributes(
            attrs,
            R.styleable.StarRatingView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                currentRating = getFloat(R.styleable.StarRatingView_rating, 1f)
                ratingStep = getFloat(R.styleable.StarRatingView_step, RATING_STEP)
                require(ratingStep in 0.1..1.0) {
                    "ratingStep should be in [0.1 ... 1.0]"
                }
                gapSize = getDimensionPixelSize(R.styleable.StarRatingView_gap, GAP_SIZE)
                maxRating = getInt(R.styleable.StarRatingView_maxRating, MAX_RATING)

                selectedIcon = getDrawable(R.styleable.StarRatingView_selectedIcon)
                    ?: ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.ic_round_star_24,
                        context.theme
                    )!!
                notSelectedIcon = getDrawable(R.styleable.StarRatingView_unselectedIcon)
                    ?: ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.ic_round_star_outline_24,
                        context.theme
                    )!!
                require(
                    selectedIcon.intrinsicWidth == notSelectedIcon.intrinsicWidth &&
                            selectedIcon.intrinsicHeight == notSelectedIcon.intrinsicHeight
                ) {
                    "selectedIcon and notSelectedIcon should have same size"
                }

                getColor(R.styleable.StarRatingView_iconTint, ICON_TINT_COLOR).let { iconTint ->
                    selectedIcon.setTint(iconTint)
                    notSelectedIcon.setTint(iconTint)
                }

            } finally {
                recycle()
            }
        }
    }

    fun setOnRatingChangedListener(onRatingBarChangeListener: OnRatingChangedListener) {
        listener = onRatingBarChangeListener
    }

    @UiThread
    fun setRating(@FloatRange(from = 0.0) rating: Float) {
        currentRating = min(
            maxRating.toFloat(),
            max(0f, rating)
        )
        invalidate()
    }

    private fun updateRating(@FloatRange(from = 0.0) rating: Float) {
        currentRating = rating
        listener.onRatingChanged(currentRating)
        invalidate()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                val drawableWidth = notSelectedIcon.bounds.width()
                val eventX = event.x - paddingLeft
                val flooredItemsNum = (eventX / (drawableWidth + gapSize)).toInt()
                val fractionalItemValue = (eventX % (drawableWidth + gapSize)) / drawableWidth
                val steppedFractionalValue =
                    ((fractionalItemValue / ratingStep).toInt() + 1) * ratingStep
                updateRating(
                    min(
                        maxRating.toFloat(),
                        max(0f, flooredItemsNum + steppedFractionalValue)
                    )
                )
            }
        }
        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val measuredHeight: Int = measureHeight(heightMeasureSpec)
        val measuredWidth: Int = measureWidth(widthMeasureSpec)
        if (selectedIcon.intrinsicHeight > measuredHeight) {
            selectedIcon.setBounds(0, 0, measuredHeight, measuredHeight)
            notSelectedIcon.setBounds(0, 0, measuredHeight, measuredHeight)
        } else {
            // View height > single icon height
            // scale icon to match height

            val scaleY =
                measuredHeight.toFloat() - paddingTop - paddingBottom / selectedIcon.intrinsicHeight
            val scaleX = (measuredWidth.toFloat() - paddingLeft - paddingRight -
                    (maxRating - 1f) * gapSize) / (selectedIcon.intrinsicWidth * maxRating)

            val scale = min(scaleX, scaleY)

            selectedIcon.setBounds(
                0,
                0,
                (selectedIcon.intrinsicWidth * scale).toInt(),
                (selectedIcon.intrinsicHeight * scale).toInt()
            )
            notSelectedIcon.setBounds(
                0,
                0,
                (notSelectedIcon.intrinsicWidth * scale).toInt(),
                (notSelectedIcon.intrinsicHeight * scale).toInt()
            )
        }
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.translate(paddingLeft.toFloat(), paddingTop.toFloat())

        val iconBounds = selectedIcon.bounds
        val flooredRating = floor(currentRating).toInt()
        // Draw floored
        var ratingIconIdx = 0
        while (ratingIconIdx < flooredRating) {
            selectedIcon.draw(canvas)
            canvas.translate((gapSize + iconBounds.width()).toFloat(), 0f)
            ratingIconIdx++
        }

        // Draw fractional
        if (ratingIconIdx < maxRating) {
            val fractionalRating = currentRating - flooredRating
            val fractionalItemWidth: Int = (iconBounds.width() * fractionalRating).toInt()
            val fractionalItemHeight: Int = iconBounds.height()

            canvas.save()
            canvas.clipRect(0, 0, fractionalItemWidth, fractionalItemHeight)
            selectedIcon.draw(canvas)
            canvas.restore()

            canvas.save()
            canvas.clipRect(fractionalItemWidth, 0, iconBounds.right, iconBounds.bottom)
            notSelectedIcon.draw(canvas)
            canvas.restore()
            canvas.translate((gapSize + iconBounds.width()).toFloat(), 0f)
            ratingIconIdx++
        }

        // Draw unselected
        while (ratingIconIdx < maxRating) {
            notSelectedIcon.draw(canvas)
            canvas.translate((gapSize + iconBounds.width()).toFloat(), 0f)
            ratingIconIdx++
        }
    }

    private fun measureWidth(widthMeasureSpec: Int): Int {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        if (widthMode == MeasureSpec.EXACTLY) return widthSize

        val drawableWidth = max(selectedIcon.intrinsicWidth, selectedIcon.bounds.width())
        val maxSize =
            (paddingLeft + paddingRight + (maxRating - 1f) * gapSize + maxRating * drawableWidth).toInt()
        return if (widthMode == MeasureSpec.AT_MOST) {
            maxSize.coerceAtMost(widthSize)
        } else {
            maxSize
        }
    }

    private fun measureHeight(heightMeasureSpec: Int): Int {
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        if (heightMode == MeasureSpec.EXACTLY) return heightSize

        val drawableHeight = max(selectedIcon.intrinsicHeight, selectedIcon.bounds.height())
        val maxHeight = paddingBottom + paddingTop + drawableHeight
        return if (heightMode == MeasureSpec.AT_MOST) {
            heightSize.coerceAtMost(maxHeight)
        } else {
            maxHeight
        }
    }

    companion object {
        private const val MAX_RATING = 5
        private const val RATING_STEP = 0.5f
        private const val GAP_SIZE = 0
        private const val ICON_TINT_COLOR = Color.YELLOW
    }
}