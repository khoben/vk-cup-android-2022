package com.github.khoben.poll

typealias MultiStagePoll = List<Poll>

enum class OptionState {
    UNANSWERED,
    INCORRECT,
    CORRECT
}

data class Option(
    val id: String,
    val caption: String,
    val votes: Long,
    val state: OptionState = OptionState.UNANSWERED
)

data class Poll(
    val id: String,
    val question: String,
    val options: List<Option>,
    val isAnswered: Boolean = false
)
