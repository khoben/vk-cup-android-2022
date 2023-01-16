package com.github.khoben.zen_elements.ui.fill

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.khoben.fill_blanks.State
import com.github.khoben.zen_elements.databinding.FillFragmentLayoutBinding
import com.github.khoben.zen_elements.ui.base.BaseFragment

class FillFragment : BaseFragment<FillFragmentLayoutBinding>() {

    private val rightAnswers = listOf<String>("ipsum", "Praesent", "tellus", "auctor")

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FillFragmentLayoutBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fillBlanks.updateText(
            "Lorem [ipsum] dolor sit amet, " +
                    "consectetur adipiscing elit. " +
                    "[Praesent] blandit ipsum arcu, " +
                    "nec elementum lacus euismod ullamcorper. " +
                    "Etiam [tellus] turpis, [auctor] quis sapien " +
                    "sit amet, lobortis lacinia leo."
        )

        binding.check.setOnClickListener {
            binding.fillBlanks.validate(
                binding.fillBlanks.getUserAnswers().mapIndexed { index, s ->
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
        val TAG: String = FillFragment::class.java.simpleName
    }
}