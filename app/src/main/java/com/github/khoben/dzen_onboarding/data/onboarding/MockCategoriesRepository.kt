package com.github.khoben.dzen_onboarding.data.onboarding

import com.github.khoben.dzen_onboarding.domain.onboarding.CategoriesRepository

class MockCategoriesRepository(
    private val dataSource: CategoriesDataSource
) : CategoriesRepository {
    override suspend fun fetch(): Result<Categories> {
        return runCatching { dataSource.fetch() }
    }
}