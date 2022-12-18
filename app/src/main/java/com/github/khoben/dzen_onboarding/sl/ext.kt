package com.github.khoben.dzen_onboarding.sl

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.github.khoben.dzen_onboarding.App

/**
 * Get app-wise DI container
 *
 * Must be call after attached state
 */
val Fragment.diContainer: Container get() = requireActivity().diContainer

/**
 * Get app-wise DI container
 *
 * Must be call after attached state
 */
val FragmentActivity.diContainer: Container get() = (application as App).appContainer