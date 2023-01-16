package com.github.khoben.zen_elements.common

import android.content.Context
import androidx.annotation.StringRes

sealed class PlatformString {
    abstract fun string(context: Context): String

    class Plain(private val text: String) : PlatformString() {
        override fun string(context: Context) = text
    }

    class Resource(@StringRes private val resId: Int) : PlatformString() {
        override fun string(context: Context) = context.getString(resId)
    }

    class Arguments(
        @StringRes private val resId: Int,
        private vararg val args: Any
    ) : PlatformString() {
        override fun string(context: Context) =
            context.getString(
                resId,
                *args.map { if (it is PlatformString) it.string(context) else it }.toTypedArray()
            )
    }
}