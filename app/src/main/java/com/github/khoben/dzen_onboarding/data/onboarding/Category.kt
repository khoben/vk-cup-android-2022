package com.github.khoben.dzen_onboarding.data.onboarding

typealias Categories = List<Category>

data class Category(
    val id: Long,
    val caption: String
)
