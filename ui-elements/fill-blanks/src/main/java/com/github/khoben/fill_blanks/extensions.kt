package com.github.khoben.fill_blanks

import android.content.res.Resources
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat

fun View.hideKeyboard() {
    val inputMethodManager =
        ContextCompat.getSystemService(context, InputMethodManager::class.java)!!
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    clearFocus()
}

/**
 * Convert dp to px
 */
internal val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

/**
 * Convert sp to px
 */
internal val Int.sp: Int
    get() = (this * Resources.getSystem().displayMetrics.scaledDensity).toInt()