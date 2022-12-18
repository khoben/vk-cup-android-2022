package com.github.khoben.dzen_onboarding.domain.onboarding

import com.github.khoben.dzen_onboarding.data.onboarding.Categories
import com.github.khoben.dzen_onboarding.domain.base.Mapper
import com.github.khoben.dzen_onboarding.domain.base.UseCase

class FetchCategoriesUseCase(
    private val repository: CategoriesRepository,
    private val mapper: Mapper<Categories, CategoriesDomain>
) : UseCase<CategoriesDomain> {
    override suspend fun invoke(): Result<CategoriesDomain> {
        return repository.fetch().map { mapper.map(it) }
    }
}