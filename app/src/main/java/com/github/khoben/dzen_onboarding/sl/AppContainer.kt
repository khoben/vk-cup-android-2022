package com.github.khoben.dzen_onboarding.sl

import android.content.Context
import com.github.khoben.dzen_onboarding.R
import com.github.khoben.dzen_onboarding.data.onboarding.CategoriesDataSource
import com.github.khoben.dzen_onboarding.data.onboarding.Category
import com.github.khoben.dzen_onboarding.data.onboarding.MockCategoriesRepository
import com.github.khoben.dzen_onboarding.domain.base.UseCase
import com.github.khoben.dzen_onboarding.domain.onboarding.CategoriesDomain
import com.github.khoben.dzen_onboarding.domain.onboarding.CategoriesToDomainMapper
import com.github.khoben.dzen_onboarding.domain.onboarding.FetchCategoriesUseCase
import com.github.khoben.dzen_onboarding.domain.onboarding.I18nCategoryMapper

class AppContainer(private val context: Context) : Container {
    override val fetchCategoriesUseCase: UseCase<CategoriesDomain> by lazy {
        FetchCategoriesUseCase(
            repository = MockCategoriesRepository(
                CategoriesDataSource.Mock(
                    data = mockedCategories
                )
            ),
            mapper = CategoriesToDomainMapper(
                I18nCategoryMapper.Mock(
                    i18nMap = mockedI18nCategories
                )
            )
        )
    }

    companion object {
        private val mockedCategories = listOf(
            Category(id = 1, "humor"),
            Category(id = 2, "eating"),
            Category(id = 3, "movies"),
            Category(id = 4, "restaurants"),
            Category(id = 5, "walking"),
            Category(id = 6, "politics"),
            Category(id = 7, "news"),
            Category(id = 8, "auto"),
            Category(id = 9, "serials"),
            Category(id = 10, "recipes"),
            Category(id = 11, "work"),
            Category(id = 12, "relax"),
            Category(id = 13, "sport"),
            Category(id = 14, "humor"),
            Category(id = 15, "eating"),
            Category(id = 16, "movies"),
            Category(id = 17, "restaurants"),
            Category(id = 18, "walking"),
            Category(id = 19, "politics"),
            Category(id = 20, "news"),
            Category(id = 21, "auto"),
            Category(id = 22, "serials"),
            Category(id = 23, "recipes"),
            Category(id = 24, "work"),
            Category(id = 25, "relax"),
            Category(id = 26, "sport"),
        )

        private val mockedI18nCategories = mapOf(
            "humor" to R.string.humor,
            "eating" to R.string.eating,
            "movies" to R.string.movies,
            "restaurants" to R.string.restaurants,
            "walking" to R.string.walking,
            "politics" to R.string.politics,
            "news" to R.string.news,
            "auto" to R.string.auto,
            "serials" to R.string.serials,
            "recipes" to R.string.recipes,
            "work" to R.string.work,
            "relax" to R.string.relax,
            "sport" to R.string.sport
        )
    }
}