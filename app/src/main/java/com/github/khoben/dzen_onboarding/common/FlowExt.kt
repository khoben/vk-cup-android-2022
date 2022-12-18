package com.github.khoben.dzen_onboarding.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

/**
 * Prepend [initial] to flow as first emitted value
 */
internal fun <T> Flow<T>.initial(initial: T): Flow<T> = flow {
    emit(initial)
    emitAll(this@initial)
}