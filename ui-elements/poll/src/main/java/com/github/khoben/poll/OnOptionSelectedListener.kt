package com.github.khoben.poll

interface OnOptionSelectedListener {
    fun onOptionSelected(pollId: String, optionId: String)

    object NOOP : OnOptionSelectedListener {
        override fun onOptionSelected(pollId: String, optionId: String) = Unit
    }
}