package com.github.khoben.draggable_match

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.github.khoben.draggable_match.databinding.ItemLayoutBinding
import java.util.*

internal class DraggableAdapter(
    private val draggable: Boolean = true,
    private val items: MutableList<Item> = mutableListOf()
) : RecyclerView.Adapter<DraggableAdapter.ItemViewHolder>(), SwapItems {

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(items: List<Item>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun getOrder(): List<String> {
        return items.map { it.id }
    }

    override fun swapItems(from: Int, to: Int) {
        Collections.swap(items, from, to)
        notifyItemMoved(from, to)
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ItemViewHolder(
        private val binding: ItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            binding.text.text = item.title
            if (!draggable) {
                binding.rightIcon.isVisible = false
            }
        }
    }

}