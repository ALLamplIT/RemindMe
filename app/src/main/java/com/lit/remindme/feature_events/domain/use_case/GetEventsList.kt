package com.lit.remindme.feature_events.domain.use_case

import com.lit.remindme.feature_events.domain.model.Event
import com.lit.remindme.feature_events.domain.repository.EventRepository
import com.lit.remindme.feature_events.util.EventsOrderType
import com.lit.remindme.feature_events.util.OrderDirection
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class GetEventsList(
    private val repository: EventRepository
) {
    suspend operator fun invoke(
        eventsOrderType: EventsOrderType = EventsOrderType.Date(OrderDirection.Descending)
    ): List<Event> {
        return repository.getEventsList().let { events ->

            when(eventsOrderType.orderDirection){
                is OrderDirection.Ascending -> {
                    when(eventsOrderType) {
                        is EventsOrderType.Title -> events.sortedBy { it.title.lowercase() }
                        is EventsOrderType.Date -> events.sortedBy {
                        val formatter = DateTimeFormatter.ofPattern("MM-dd")
                            LocalDate.parse(it.eventDate).format(formatter)
                        }
                        is EventsOrderType.Type -> events.sortedBy { it.eventType }
                    }
                }
                is OrderDirection.Descending -> {
                    when(eventsOrderType) {
                        is EventsOrderType.Title -> events.sortedByDescending { it.title.lowercase() }
                        is EventsOrderType.Date -> events.sortedByDescending {
                            val formatter = DateTimeFormatter.ofPattern("MM-dd")
                            LocalDate.parse(it.eventDate).format(formatter)
                        }
                        is EventsOrderType.Type -> events.sortedByDescending { it.eventType }
                    }
                }
            }
        }
    }
}