package com.github.khoben.dzen_onboarding.ui.screen.onboarding

import android.widget.Toast
import com.github.khoben.dzen_onboarding.common.PlatformString
import com.github.khoben.dzen_onboarding.domain.onboarding.CategoriesDomain
import com.github.khoben.dzen_onboarding.ui.screen.base.state.ViewState
import com.github.khoben.dzen_onboarding.ui.screen.onboarding.adapter.CategoriesUi

interface OnBoardingViewState : ViewState {
    data class UiState(val dataState: DataState) : ViewState.UiState {
        companion object {
            val EMPTY = UiState(dataState = DataState.Empty)
        }
    }

    sealed class DataState {
        class List(
            val data: CategoriesUi,
            val showContinue: Boolean
        ) : DataState()
        class Error(val error: Throwable? = null) : DataState()
        object Empty : DataState()
    }

    sealed class UiEffect : ViewState.UiEffect {
        class ShowToast(
            val message: PlatformString,
            val duration: Int = Toast.LENGTH_SHORT
        ) : UiEffect()
    }
}