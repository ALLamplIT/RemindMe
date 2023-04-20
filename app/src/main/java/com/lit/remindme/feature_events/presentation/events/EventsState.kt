package com.lit.remindme.feature_events.presentation.events

import com.lit.remindme.feature_events.domain.model.Event
import com.lit.remindme.feature_events.util.EventsOrderType
import com.lit.remindme.feature_events.util.OrderDirection

data class EventsState(
//    val events: List<Event> = emptyList(),
    val eventsOrderType: EventsOrderType = EventsOrderType.Date(OrderDirection.Descending),
    val dailyReminderTime:String = "11:00",
    val isOrderSectionVisible: Boolean = false
)