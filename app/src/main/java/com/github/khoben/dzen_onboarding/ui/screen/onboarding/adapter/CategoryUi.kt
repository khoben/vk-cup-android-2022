package com.github.khoben.dzen_onboarding.ui.screen.onboarding.adapter

import com.github.khoben.dzen_onboarding.common.PlatformString

typealias CategoriesUi = List<CategoryUi>

data class CategoryUi(
    val id: Long,
    val caption: PlatformString,
    val isSelected: Boolean = false
)
