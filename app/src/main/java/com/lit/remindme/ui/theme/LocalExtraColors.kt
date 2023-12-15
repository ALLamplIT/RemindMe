package com.lit.remindme.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color


data class ExtraColors(
    val listViewBackground1: Color = Color.Unspecified,
    val listViewBackground2: Color = Color.Unspecified,
    val listViewHighlight1: Color = Color.Unspecified,
    val listViewHighlight2: Color = Color.Unspecified
)

val LocalExtraColors = staticCompositionLocalOf { ExtraColors() }
