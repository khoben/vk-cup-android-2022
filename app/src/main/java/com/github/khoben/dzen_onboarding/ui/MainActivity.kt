package com.github.khoben.dzen_onboarding.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.khoben.dzen_onboarding.R
import com.github.khoben.dzen_onboarding.ui.screen.onboarding.OnBoardingFragment

class MainActivity : AppCompatActivity(R.layout.main_container) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container, OnBoardingFragment(), OnBoardingFragment.TAG)
                .commit()
        }
    }
}