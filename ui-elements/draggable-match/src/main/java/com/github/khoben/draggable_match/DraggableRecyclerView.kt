package com.github.khoben.draggable_match

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

internal class DraggableRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : RecyclerView(context, attrs, defStyleAttr) {

    private val draggableAdapter: DraggableAdapter
    private val draggable: Boolean

    init {
        context.obtainStyledAttributes(
            attrs, R.styleable.DraggableRecyclerView, defStyleAttr, 0
        ).apply {
            try {
                draggable = getBoolean(R.styleable.DraggableRecyclerView_draggable, true)
            } finally {
                recycle()
            }
        }

        draggableAdapter = DraggableAdapter(draggable = draggable)

        adapter = draggableAdapter
        layoutManager = LinearLayoutManager(context)

        if (draggable) {
            ItemTouchHelper(
                DraggableCallback(
                    draggableAdapter,
                    ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                    0
                )
            ).attachToRecyclerView(this)
        }
    }

    fun updateData(data: List<Item>) = draggableAdapter.updateData(data)

    fun getOrder() = draggableAdapter.getOrder()
}