package com.github.khoben.poll

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorInt
import androidx.annotation.UiThread
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.HandlerCompat
import androidx.core.os.postDelayed
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DefaultItemAnimator
import com.github.khoben.poll.adapter.OptionUi
import com.github.khoben.poll.adapter.OptionsAdapter
import com.github.khoben.poll.databinding.PollLayoutBinding

class MultiStagePollView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    private var onOptionSelectedListener: OnOptionSelectedListener = OnOptionSelectedListener.NOOP

    private val pollBinding = PollLayoutBinding.inflate(LayoutInflater.from(context), this)
    private val pollAdapter: OptionsAdapter = OptionsAdapter {
        onOptionSelectedListener.onOptionSelected(it.pollId, it.optionId)
    }
    private val pollHandler: Handler = HandlerCompat.createAsync(Looper.getMainLooper())

    @ColorInt
    private val defaultBgColor: Int

    @ColorInt
    private val correctBgColor: Int

    @ColorInt
    private val incorrectBgColor: Int

    private val correctIcon: Drawable
    private val incorrectIcon: Drawable

    private val optionItemMarginPx: Int

    private val stageTimeout: Int
    private var currentStageIdx = 0

    init {
        orientation = VERTICAL

        with(pollBinding.optionList) {
            this.adapter = pollAdapter
            (this.itemAnimator as DefaultItemAnimator)
                .supportsChangeAnimations = false
        }

        context.obtainStyledAttributes(
            attrs, R.styleable.MultiStagePollView, defStyleAttr, 0
        ).apply {
            try {
                stageTimeout = getInt(
                    R.styleable.MultiStagePollView_stageTimeout, DEFAULT_TIMEOUT
                )

                optionItemMarginPx = getDimensionPixelSize(
                    R.styleable.MultiStagePollView_itemsVerticalMargin, DEFAULT_ITEM_MARGIN
                )

                defaultBgColor = getColor(
                    R.styleable.MultiStagePollView_defaultBackground, ResourcesCompat.getColor(
                        context.resources, R.color.default_option_color, context.theme
                    )
                )

                correctBgColor = getColor(
                    R.styleable.MultiStagePollView_correctBackground, ResourcesCompat.getColor(
                        context.resources, R.color.correct_option_color, context.theme
                    )
                )

                incorrectBgColor = getColor(
                    R.styleable.MultiStagePollView_incorrectBackground, ResourcesCompat.getColor(
                        context.resources, R.color.incorrect_option_color, context.theme
                    )
                )

                correctIcon = getDrawable(R.styleable.MultiStagePollView_correctIcon)
                    ?: ResourcesCompat.getDrawable(
                        context.resources, R.drawable.ic_round_check_24, context.theme
                    )!!
                incorrectIcon = getDrawable(R.styleable.MultiStagePollView_incorrectIcon)
                    ?: ResourcesCompat.getDrawable(
                        context.resources, R.drawable.ic_round_close_24, context.theme
                    )!!

                getColor(
                    R.styleable.MultiStagePollView_correctIconTint, ResourcesCompat.getColor(
                        context.resources, R.color.correct_icon_color, context.theme
                    )
                ).let { iconTint ->
                    correctIcon.setTint(iconTint)
                }

                getColor(
                    R.styleable.MultiStagePollView_incorrectIconTint, ResourcesCompat.getColor(
                        context.resources, R.color.incorrect_icon_color, context.theme
                    )
                ).let { iconTint ->
                    incorrectIcon.setTint(iconTint)
                }

            } finally {
                recycle()
            }
        }
    }


    fun setOnOptionSelectedListener(onOptionSelectedListener: OnOptionSelectedListener) {
        this.onOptionSelectedListener = onOptionSelectedListener
    }

    @UiThread
    fun updatePoll(poll: MultiStagePoll) {
        require(poll.isNotEmpty()) {
            "poll should has at least one question"
        }

        val currentStage = poll[currentStageIdx]

        require(currentStage.options.isNotEmpty() && currentStage.options.size < MAX_OPTIONS) {
            "poll stage should has at least one and less than $MAX_OPTIONS options"
        }

        if (!currentStage.isAnswered) {
            pollBinding.stageTimeoutProgress.isVisible = false
            pollBinding.optionList.isClickable = true
        }

        pollBinding.stage.text =
            context.getString(R.string.question_out_of, currentStageIdx + 1, poll.size)
        pollBinding.question.text = currentStage.question

        pollAdapter.submitList(currentStage.options.map {
            OptionUi(
                pollId = currentStage.id,
                optionId = it.id,
                caption = it.caption,
                background = when (it.state) {
                    OptionState.UNANSWERED -> defaultBgColor
                    OptionState.INCORRECT -> incorrectBgColor
                    OptionState.CORRECT -> correctBgColor
                },
                drawable = when (it.state) {
                    OptionState.UNANSWERED -> null
                    OptionState.INCORRECT -> incorrectIcon
                    OptionState.CORRECT -> correctIcon
                },
                isAnswered = currentStage.isAnswered,
                percent = it.votes.toInt() // )
            )
        })

        if (currentStage.isAnswered && currentStageIdx + 1 < poll.size) {
            currentStageIdx++
            pollBinding.stageTimeoutProgress.isVisible = true

            pollHandler.removeCallbacksAndMessages(null)
            pollHandler.postDelayed(stageTimeout.toLong()) {
                updatePoll(poll)
            }
        }
    }

    companion object {
        private val DEFAULT_ITEM_MARGIN = 8.dp
        private const val MAX_OPTIONS = 10
        private const val DEFAULT_TIMEOUT = 2_300
    }
}