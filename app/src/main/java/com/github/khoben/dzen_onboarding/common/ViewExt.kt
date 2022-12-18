package com.github.khoben.dzen_onboarding.common

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.widget.ImageView
import androidx.annotation.ColorInt
import com.google.android.material.card.MaterialCardView

fun MaterialCardView.bgColorTransition(@ColorInt endColor: Int, duration: Long = 250L) {
    var startColor = Color.TRANSPARENT
    if (background is ColorDrawable)
        startColor = (background as ColorDrawable).color
    ValueAnimator.ofObject(ArgbEvaluator(), startColor, endColor)
        .setDuration(duration)
        .apply {
            addUpdateListener {
                if (it.animatedValue is Int) {
                    val color = it.animatedValue as Int
                    setCardBackgroundColor(color)
                }
            }
        }.start()
}