package com.github.khoben.dzen_onboarding.ui.component

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.core.content.ContextCompat
import com.github.khoben.dzen_onboarding.R
import com.github.khoben.dzen_onboarding.common.bgColorTransition
import com.google.android.material.card.MaterialCardView

class CategoryView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : MaterialCardView(context, attributeSet, defStyleAttr) {

    // TODO: Read from attribute set
    private val inactiveColor = ContextCompat.getColor(context, R.color.chip_inactive_background)
    private val activeColor = ContextCompat.getColor(context, R.color.chip_active_background)

    private val inactiveIcon = ContextCompat.getDrawable(context, R.drawable.ic_round_add_24)!!
    private val activeIcon = ContextCompat.getDrawable(context, R.drawable.ic_round_check_24)!!

    private lateinit var divider: View
    private lateinit var icon: ImageView

    init {
        setCardBackgroundColor(inactiveColor)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        divider = findViewById(R.id.divider)
        icon = findViewById(R.id.right_icon)
    }

    fun setChosen(selected: Boolean) {
        bgColorTransition(if (selected) activeColor else inactiveColor, DEFAULT_ANIM_DURATION)
        icon.setImageDrawable(if (selected) activeIcon else inactiveIcon)
        divider.animate()
            .alpha(if (selected) 0f else 1f)
            .setDuration(DEFAULT_ANIM_DURATION)
            .start()
    }

    companion object {
        private const val DEFAULT_ANIM_DURATION = 250L
    }
}