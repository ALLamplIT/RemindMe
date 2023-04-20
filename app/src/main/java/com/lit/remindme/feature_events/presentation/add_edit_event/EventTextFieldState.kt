package com.lit.remindme.feature_events.presentation.add_edit_event

data class EventTextFieldState(
    val text: String = "",
    val hint: Int = 0,
    val isHintVisible: Boolean = true
)