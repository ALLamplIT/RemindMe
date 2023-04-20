package com.lit.remindme.feature_events.presentation.util

sealed class Screen(val route: String) {
    object EventsScreen: Screen("events_screen")
    object AddEditEventScreen: Screen("add_edit_event_screen")
}
