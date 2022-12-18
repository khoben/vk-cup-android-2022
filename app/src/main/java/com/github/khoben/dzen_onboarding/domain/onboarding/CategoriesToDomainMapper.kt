package com.github.khoben.dzen_onboarding.domain.onboarding

import com.github.khoben.dzen_onboarding.data.onboarding.Categories
import com.github.khoben.dzen_onboarding.domain.base.Mapper

class CategoriesToDomainMapper(
    private val i18nCategoryMapper: I18nCategoryMapper
) : Mapper<Categories, CategoriesDomain> {
    override fun map(data: Categories): CategoriesDomain {
        return data.map { CategoryDomain(it.id, i18nCategoryMapper.map(it.caption)) }
    }
}