package com.github.khoben.poll.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.khoben.poll.R
import com.github.khoben.poll.databinding.OptionLayoutBinding

class OptionsAdapter(
    private val onItemClicked: (OptionUi) -> Unit
) : ListAdapter<OptionUi, OptionsAdapter.OptionHolder>(OptionCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionHolder {
        return OptionHolder(
            OptionLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OptionHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class OptionHolder(
        private val binding: OptionLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: OptionUi) {
            binding.optionText.text = item.caption
            binding.root.backgroundTintList = ColorStateList.valueOf(item.background)
            binding.optionRightIcon.setImageDrawable(item.drawable)

            if (item.isAnswered) {
                binding.optionPercentage.isVisible = true
                binding.optionPercentage.text =
                    itemView.context.getString(R.string.percent, item.percent)
                binding.root.setOnClickListener(null)
            } else {
                binding.optionPercentage.isVisible = false
                binding.root.setOnClickListener { onItemClicked.invoke(item) }
            }
        }
    }
}