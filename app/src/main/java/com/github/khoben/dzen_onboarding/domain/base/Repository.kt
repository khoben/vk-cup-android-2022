package com.github.khoben.dzen_onboarding.domain.base

interface Repository<D> {
    suspend fun fetch(): Result<D>
}