package com.github.khoben.dzen_onboarding.ui.screen.base.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<State : ViewState.UiState, Effect : ViewState.UiEffect> : ViewModel() {

    private val initialState: State by lazy { createInitialState() }

    private val _uiState = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    private val _uiEffects: Channel<Effect> = Channel(Channel.BUFFERED)
    val uiEffects = _uiEffects.receiveAsFlow()

    abstract fun createInitialState(): State

    protected fun setState(reduce: State.() -> State) {
        _uiState.update(reduce)
    }

    protected fun setEffect(effect: () -> Effect) {
        viewModelScope.launch { _uiEffects.send(effect()) }
    }
}