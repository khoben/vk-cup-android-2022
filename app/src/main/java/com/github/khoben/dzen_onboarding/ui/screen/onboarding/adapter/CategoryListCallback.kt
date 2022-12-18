package com.github.khoben.dzen_onboarding.ui.screen.onboarding.adapter

import androidx.recyclerview.widget.DiffUtil

class CategoryListCallback : DiffUtil.ItemCallback<CategoryUi>() {
    override fun areItemsTheSame(
        oldItem: CategoryUi,
        newItem: CategoryUi
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: CategoryUi,
        newItem: CategoryUi
    ): Boolean {
        return oldItem == newItem
    }
}