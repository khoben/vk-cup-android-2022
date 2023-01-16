package com.github.khoben.poll.adapter

import androidx.recyclerview.widget.DiffUtil

class OptionCallback : DiffUtil.ItemCallback<OptionUi>() {
    override fun areItemsTheSame(
        oldItem: OptionUi,
        newItem: OptionUi
    ): Boolean {
        return oldItem.pollId == newItem.pollId &&
                oldItem.optionId == newItem.optionId
    }

    override fun areContentsTheSame(
        oldItem: OptionUi,
        newItem: OptionUi
    ): Boolean {
        return oldItem == newItem
    }
}