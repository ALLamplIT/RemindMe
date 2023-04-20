package com.lit.remindme.feature_events.presentation.util

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class UIString{
    data class DirectString(val value: String): UIString()
    class StringFromResource(
        @StringRes val resourceID: Int,
        vararg val args: Any
    ): UIString()

    @Composable
    fun getString(): String {
        return when (this) {
            is DirectString -> value
            is StringFromResource -> stringResource(resourceID, *args)
        }
    }

    fun getString(context: Context): String {
        return when (this) {
            is DirectString -> value
            is StringFromResource -> context.getString(resourceID, *args)
        }
    }

}
