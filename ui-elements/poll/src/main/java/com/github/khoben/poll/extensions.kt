package com.github.khoben.poll

import android.content.res.Resources

/**
 * Convert dp to px
 */
internal val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()