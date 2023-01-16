package com.github.khoben.poll.adapter

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt

data class OptionUi(
    val pollId: String,
    val optionId: String,
    val caption: String,
    @ColorInt val background: Int,
    val drawable: Drawable? = null,
    val isAnswered: Boolean = false,
    val percent: Int = 0
)
