package com.github.khoben.zen_elements.ui.match

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.khoben.draggable_match.Item
import com.github.khoben.draggable_match.State
import com.github.khoben.zen_elements.databinding.MatchFragmentLayoutBinding
import com.github.khoben.zen_elements.ui.base.BaseFragment

class MatchFragment : BaseFragment<MatchFragmentLayoutBinding>() {

    private val rightAnswers = listOf<String>("1", "2", "3", "4")

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = MatchFragmentLayoutBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.matchView.updateData(
            listOf(
                Item("First option", "1"),
                Item("Second option", "2"),
                Item("Third option", "3"),
                Item("4th", "4"),
            )
        )

        binding.check.setOnClickListener {
            binding.matchView.validate(
                binding.matchView.getOrder().mapIndexed { index, s ->
                    if (s == rightAnswers[index]) {
                        State.VALID
                    } else {
                        State.INVALID
                    }
                }
            )
        }
    }

    companion object {
        val TAG: String = MatchFragment::class.java.simpleName
    }
}