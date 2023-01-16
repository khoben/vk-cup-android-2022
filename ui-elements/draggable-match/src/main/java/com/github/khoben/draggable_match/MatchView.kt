package com.github.khoben.draggable_match

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.doOnLayout
import androidx.core.view.doOnPreDraw
import androidx.core.view.size
import com.github.khoben.draggable_match.databinding.MatchLayoutBinding
import kotlin.math.max

class MatchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    private val matchBinding = MatchLayoutBinding.inflate(LayoutInflater.from(context), this)
    private val defaultItemColor = ContextCompat.getColor(context, R.color.default_item_color)
    private val incorrectItemColor = ContextCompat.getColor(context, R.color.incorrect_item_color)
    private val correctItemColor = ContextCompat.getColor(context, R.color.correct_item_color)

    init {
        orientation = HORIZONTAL
    }

    fun updateData(data: List<Item>) {
        matchBinding.draggablePart.updateData(data)
        matchBinding.undraggablePart.updateData(data)
        setupMatchVertical()
    }

    fun getOrder() = matchBinding.draggablePart.getOrder()

    fun validate(data: List<State>) {
        require(data.size == matchBinding.draggablePart.size) {
            "Size mismatch data.size=${data.size} != views.size=${matchBinding.draggablePart.size}"
        }

        data.forEachIndexed { idx, state ->
            matchBinding.draggablePart.getChildAt(idx).backgroundColor(
                when (state) {
                    State.NONE -> defaultItemColor
                    State.VALID -> correctItemColor
                    State.INVALID -> incorrectItemColor
                }
            )
        }
    }

    private fun setupMatchVertical() {
        doOnLayout {
            val maxItemHeight = max(
                matchBinding.draggablePart.children.maxOf { it.measuredHeight },
                matchBinding.undraggablePart.children.maxOf { it.measuredHeight },
            )
            if (maxItemHeight > 0) {
                val matchDecoration = MatchVerticalItemDecoration(maxItemHeight)
                matchBinding.draggablePart.addItemDecoration(matchDecoration)
                matchBinding.undraggablePart.addItemDecoration(matchDecoration)
            }
        }
    }

    private fun View.backgroundColor(@ColorInt color: Int) {
        background.mutate().colorFilter =
            PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }
}