package com.github.khoben.dzen_onboarding.ui.screen.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.github.khoben.dzen_onboarding.R
import com.github.khoben.dzen_onboarding.common.PlatformString
import com.github.khoben.dzen_onboarding.common.initial
import com.github.khoben.dzen_onboarding.domain.base.UseCase
import com.github.khoben.dzen_onboarding.domain.onboarding.CategoriesDomain
import com.github.khoben.dzen_onboarding.ui.screen.base.state.BaseViewModel
import com.github.khoben.dzen_onboarding.ui.screen.onboarding.adapter.CategoryUi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class OnBoardingViewModel(
    private val fetchCategoriesUseCase: UseCase<CategoriesDomain>,
    private val uiDispatcher: CoroutineDispatcher,
    private val backgroundDispatcher: CoroutineDispatcher
) : BaseViewModel<OnBoardingViewState.UiState, OnBoardingViewState.UiEffect>() {

    override fun createInitialState() = OnBoardingViewState.UiState.EMPTY

    private val selectedCategoryIdCache = mutableSetOf<Long>()
    private val selectedFlow = MutableSharedFlow<Set<Long>>()

    init {
        viewModelScope.launch(backgroundDispatcher) {
            combine(
                flowOf(fetchCategoriesUseCase.invoke()),
                selectedFlow.initial(emptySet())
            ) { data, selected ->
                data.fold({ result ->
                    OnBoardingViewState.DataState.List(data = result.map {
                        CategoryUi(
                            it.id,
                            it.caption,
                            it.id in selected
                        )
                    }, showContinue = selected.isNotEmpty())
                }, { throwable ->
                    OnBoardingViewState.DataState.Error(throwable)
                })
            }.collectLatest {
                setState { copy(dataState = it) }
            }
        }
    }

    private fun toggleChosen(id: Long) {
        if (id in selectedCategoryIdCache) {
            selectedCategoryIdCache.remove(id)
        } else {
            selectedCategoryIdCache.add(id)
        }
        viewModelScope.launch(uiDispatcher) {
            selectedFlow.emit(selectedCategoryIdCache)
        }
    }

    private fun clearChosen() {
        selectedCategoryIdCache.clear()
        viewModelScope.launch(uiDispatcher) {
            selectedFlow.emit(selectedCategoryIdCache)
        }
    }

    fun onCategoryClicked(category: CategoryUi) = toggleChosen(category.id)

    fun onLaterClicked() {
        setEffect {
            OnBoardingViewState.UiEffect.ShowToast(PlatformString.Resource(R.string.later_clicked))
        }
    }

    fun onContinueClicked() {
        setEffect {
            OnBoardingViewState.UiEffect.ShowToast(
                PlatformString.Arguments(
                    R.string.continue_clicked, selectedCategoryIdCache.joinToString()
                )
            )
        }
        clearChosen()
    }

    class Factory(
        private val fetchCategoriesUseCase: UseCase<CategoriesDomain>,
        private val uiDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
        private val backgroundDispatcher: CoroutineDispatcher = Dispatchers.IO
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return OnBoardingViewModel(
                fetchCategoriesUseCase = fetchCategoriesUseCase,
                uiDispatcher = uiDispatcher,
                backgroundDispatcher = backgroundDispatcher
            ) as T
        }
    }
}