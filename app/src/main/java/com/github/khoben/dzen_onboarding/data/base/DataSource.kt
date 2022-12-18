package com.github.khoben.dzen_onboarding.data.base

interface DataSource<D> {
    suspend fun fetch(): D
}