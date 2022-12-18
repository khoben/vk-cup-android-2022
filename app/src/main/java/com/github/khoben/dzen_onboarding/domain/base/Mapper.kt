package com.github.khoben.dzen_onboarding.domain.base

interface Mapper<I, O> {
    fun map(data: I): O
}