package com.github.khoben.dzen_onboarding.domain.base

interface UseCase<R> {
    suspend operator fun invoke(): Result<R>
}