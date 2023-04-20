package com.lit.remindme.feature_events.domain.use_case

import com.lit.remindme.feature_events.domain.model.Event
import com.lit.remindme.feature_events.domain.repository.EventRepository
import com.lit.remindme.feature_events.domain.util.getItemColor
import com.lit.remindme.feature_events.util.EventsOrderType
import com.lit.remindme.feature_events.util.OrderDirection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class GetEvents(
    private val repository: EventRepository
) {

    operator fun invoke(
        eventsOrderType: EventsOrderType = EventsOrderType.Date(OrderDirection.Descending)
    ): Flow<List<Event>> {
        return repository.getEvents().map { events ->

//            val now = LocalDate.now()
//            var newEvents = emptyList<Event>()
//            events.forEachIndexed { index, thisEvent ->
//                newEvents = newEvents.plus(thisEvent
//                    .copy(
//                        ColorId =
//                            getItemColor(
//                                index,
//                                now,
//                                LocalDate.parse(thisEvent.eventDate).withYear(now.year)
//                            )
//                    )
//                )
//            }

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