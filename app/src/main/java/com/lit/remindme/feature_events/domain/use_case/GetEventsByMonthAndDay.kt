package com.lit.remindme.feature_events.domain.use_case

import com.lit.remindme.feature_events.domain.model.Event
import com.lit.remindme.feature_events.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetEventsByMonthAndDay(
    private val repository: EventRepository
) {

    operator fun invoke(monthAndDay: String, eventStatus: Boolean): Flow<List<Event>> {
        return repository.getEventsByMonthAndDay(monthAndDay, eventStatus).map { events ->
            events.sortedBy { it.title.lowercase() }
        }
    }

//    operator fun invoke(
//        eventsOrderType: EventsOrderType = EventsOrderType.Date(OrderDirection.Descending)
//    ): Flow<List<Event>> {
//        return repository.getEvents().map { events ->
//
//            val now = LocalDate.now()
//            var newEvents = emptyList<Event>()
//            events.forEach { thisEvent ->
//                newEvents = newEvents.plus(thisEvent.copy(ColorId = getHighlightIndicator(now,
//                    LocalDate.parse(thisEvent.eventDate).withYear(now.year))))
//            }

}