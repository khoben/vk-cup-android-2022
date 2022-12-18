package com.github.khoben.dzen_onboarding.domain.onboarding

import com.github.khoben.dzen_onboarding.common.PlatformString

typealias CategoriesDomain = List<CategoryDomain>

data class CategoryDomain(
    val id: Long,
    val caption: PlatformString
)
