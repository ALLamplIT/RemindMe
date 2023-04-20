package com.lit.remindme.feature_events.presentation.add_edit_event

import androidx.compose.ui.focus.FocusState

sealed class AddEditEventEvent {
    data class EnteredTitle(val value: String): AddEditEventEvent()
    data class EnteredEventDate(val value: String): AddEditEventEvent()
    data class ChangeTitleFocus(val focusState: FocusState): AddEditEventEvent()
    data class ChangeEventDateFocus(val focusState: FocusState): AddEditEventEvent()
    object SaveEvent: AddEditEventEvent()
}
