package com.github.khoben.dzen_onboarding.sl

import com.github.khoben.dzen_onboarding.domain.base.UseCase
import com.github.khoben.dzen_onboarding.domain.onboarding.CategoriesDomain

interface Container {
    val fetchCategoriesUseCase: UseCase<CategoriesDomain>
}