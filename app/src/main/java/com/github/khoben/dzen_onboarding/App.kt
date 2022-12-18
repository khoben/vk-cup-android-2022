package com.github.khoben.dzen_onboarding

import android.app.Application
import com.github.khoben.dzen_onboarding.sl.AppContainer
import com.github.khoben.dzen_onboarding.sl.Container

class App : Application() {
    val appContainer: Container by lazy { AppContainer(applicationContext) }
}