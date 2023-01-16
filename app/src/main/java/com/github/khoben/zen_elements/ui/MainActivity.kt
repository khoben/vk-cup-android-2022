package com.github.khoben.zen_elements.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.khoben.zen_elements.R
import com.github.khoben.zen_elements.ui.list.ListFragment

class MainActivity : AppCompatActivity(R.layout.main_layout) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.main_container, ListFragment(), ListFragment.TAG)
                .commit()
        }
    }
}