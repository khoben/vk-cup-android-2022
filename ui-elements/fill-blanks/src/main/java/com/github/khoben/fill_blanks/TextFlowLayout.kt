package com.github.khoben.fill_blanks

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.EditText


open class TextFlowLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ViewGroup(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        val heightSize: Int = MeasureSpec.getSize(heightMeasureSpec)
        val widthSizeMode: Int = MeasureSpec.getMode(widthMeasureSpec)
        val heightSizeMode: Int = MeasureSpec.getMode(heightMeasureSpec)

        var widthMax = 0
        var heightMax = 0
        var lineWidth = 0
        var lineHeight = 0

        for (childIdx in 0 until childCount) {
            val child: View = getChildAt(childIdx)
            measureChild(child, widthMeasureSpec, heightMeasureSpec)

            val layoutParams: MarginLayoutParams = child.layoutParams as MarginLayoutParams
            if (child.measuredWidth + layoutParams.leftMargin
                + layoutParams.rightMargin + lineWidth > widthSize
            ) {
                widthMax = lineWidth
                heightMax += lineHeight
                lineWidth =
                    child.measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin
                lineHeight = 0
            } else {
                lineWidth += child.measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin
            }
            var childHeight: Int =
                child.measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin
            if (child is EditText) {
                childHeight += EDITTEXT_ADDITIONAL_HEIGHT
            }
            if (childHeight > lineHeight) {
                lineHeight = childHeight
            }
            if (childIdx == childCount - 1) {
                widthMax = (child.measuredWidth + layoutParams.leftMargin
                        + layoutParams.rightMargin).coerceAtLeast(lineWidth)
                heightMax += lineHeight + EDITTEXT_ADDITIONAL_HEIGHT
            }
        }

        setMeasuredDimension(
            if (widthSizeMode == MeasureSpec.EXACTLY)
                widthSize
            else
                widthMax,
            if (heightSizeMode == MeasureSpec.EXACTLY)
                heightSize
            else
                heightMax
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var lineHeight = 0
        val lineWidth = width
        var maxHeight = 0
        var widthSum = 0

        for (i in 0 until childCount) {
            val child: View = getChildAt(i)
            val marginLayoutParams: MarginLayoutParams = child.layoutParams as MarginLayoutParams

            if (widthSum + child.measuredWidth + marginLayoutParams.leftMargin
                + marginLayoutParams.rightMargin > lineWidth
            ) {
                lineHeight += maxHeight
                widthSum = 0
            }

            if (child.measuredHeight + marginLayoutParams.topMargin
                + marginLayoutParams.bottomMargin > maxHeight
            ) {
                maxHeight =
                    child.measuredHeight + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin
            }

            if (child.visibility == GONE) {
                return
            }

            val left: Int = widthSum + marginLayoutParams.leftMargin
            val top: Int = lineHeight +
                    marginLayoutParams.topMargin
            val right: Int = (widthSum + marginLayoutParams.leftMargin
                    + child.measuredWidth)
            val bottom: Int = lineHeight +
                    marginLayoutParams.topMargin + child.measuredHeight

            child.layout(left, top, right, bottom)

            widthSum = (widthSum + child.measuredWidth + marginLayoutParams.leftMargin
                    + marginLayoutParams.rightMargin)
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?) = MarginLayoutParams(context, attrs)

    companion object {
        private const val EDITTEXT_ADDITIONAL_HEIGHT = 20
    }
}