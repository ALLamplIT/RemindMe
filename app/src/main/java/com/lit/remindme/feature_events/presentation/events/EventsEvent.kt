package com.lit.remindme.feature_events.presentation.events

import com.lit.remindme.feature_events.domain.model.Event
import com.lit.remindme.feature_events.domain.model.EventDomain
import com.lit.remindme.feature_events.util.EventsOrderType

sealed class EventsEvent {
    data class Order(val eventOrder: EventsOrderType): EventsEvent()
    data class DeleteEvent(val event: EventDomain, val index: Int): EventsEvent()
    data class DisableEvent(val event: EventDomain, val index: Int): EventsEvent()
    data class ChangeDailyRemindTime(val newTime: String): EventsEvent()
    object RestoreEvent: EventsEvent()
    object ToggleOrderSection: EventsEvent()
}