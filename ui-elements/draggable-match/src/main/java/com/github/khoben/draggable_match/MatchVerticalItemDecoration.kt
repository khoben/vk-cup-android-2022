package com.github.khoben.draggable_match

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max

internal class MatchVerticalItemDecoration(
    private val maxHeight: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val spacing = max(0, (maxHeight - view.measuredHeight) / 2)
        with(outRect) {
            top = spacing
            bottom = spacing
        }
    }
}