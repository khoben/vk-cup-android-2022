package com.github.khoben.zen_elements.ui.poll

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.khoben.poll.OnOptionSelectedListener
import com.github.khoben.poll.Option
import com.github.khoben.poll.OptionState
import com.github.khoben.poll.Poll
import com.github.khoben.zen_elements.databinding.PollFragmentLayoutBinding
import com.github.khoben.zen_elements.ui.base.BaseFragment

class PollFragment : BaseFragment<PollFragmentLayoutBinding>() {

    private var pollData = listOf(
        Poll(
            "q1",
            "Question 1",
            listOf(
                Option("a1", "First", 12L),
                Option("a2", "Second", 12L),
                Option("a3", "third", 12L),
                Option("a4", "4th", 12L)
            )
        ),
        Poll(
            "q2",
            "Question 2",
            listOf(
                Option("a1", "First", 12L),
                Option("a2", "Second", 12L),
                Option("a3", "third", 12L),
                Option("a4", "4th", 12L),
                Option("a5", "5th", 12L),
                Option("a6", "6th", 12L)
            )
        ),
        Poll(
            "q3",
            "Question 3",
            listOf(
                Option("a1", "First", 12L),
                Option("a2", "Second", 12L),
                Option("a3", "third", 12L),
                Option("a4", "4th", 12L),
                Option("a5", "5th", 12L),
                Option("a6", "6th", 12L)
            )
        )
    )

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = PollFragmentLayoutBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pollView.updatePoll(pollData)

        binding.pollView.setOnOptionSelectedListener(object : OnOptionSelectedListener {
            override fun onOptionSelected(pollId: String, optionId: String) {
                // TODO: Sample check options
                binding.pollView.updatePoll(
                    pollData.map { poll ->
                        if (poll.id == pollId) {
                            val validOptionId = poll.options.random().id
                            poll.copy(options = poll.options.map { option ->
                                if (option.id == validOptionId) {
                                    option.copy(state = OptionState.CORRECT)
                                } else {
                                    if (optionId == option.id) {
                                        option.copy(state = OptionState.INCORRECT)
                                    } else {
                                        option
                                    }
                                }
                            }, isAnswered = true)
                        } else {
                            poll
                        }
                    }.also {
                        this@PollFragment.pollData = it
                    }
                )
            }
        })
    }

    companion object {
        val TAG: String = PollFragment::class.java.simpleName
    }

}