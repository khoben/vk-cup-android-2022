package com.github.khoben.dzen_onboarding.ui.screen.onboarding.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.khoben.dzen_onboarding.databinding.CategoryRecyclerviewItemBinding
import com.github.khoben.dzen_onboarding.domain.onboarding.CategoryDomain

class CategoriesListAdapter(
    private val onItemClicked: (CategoryUi) -> Unit
) :
    ListAdapter<CategoryUi, CategoriesListAdapter.CategoryHolder>(CategoryListCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        return CategoryHolder(
            CategoryRecyclerviewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class CategoryHolder(
        private val binding: CategoryRecyclerviewItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CategoryUi) {
            binding.caption.text = item.caption.string(itemView.context)
            binding.root.setChosen(item.isSelected)
            binding.root.setOnClickListener { onItemClicked.invoke(item) }
        }
    }
}