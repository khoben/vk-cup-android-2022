package com.github.khoben.dzen_onboarding.data.onboarding

import com.github.khoben.dzen_onboarding.data.base.DataSource

interface CategoriesDataSource : DataSource<Categories> {
    class Mock(private val data: Categories): CategoriesDataSource {
        override suspend fun fetch() = data
    }
}