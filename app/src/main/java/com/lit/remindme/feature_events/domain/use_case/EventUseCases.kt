package com.lit.remindme.feature_events.domain.use_case

data class EventUseCases(
    val getEvents: GetEvents,
    val getEventsByMonthAndDay: GetEventsByMonthAndDay,
    val deleteEvent: DeleteEvent,
    val disableEvent: DisableEvent,
    val addEvent: AddEvent,
    val getEventById: GetEventById,
    val getEventByLookupId: GetEventByLookupId,
    val deleteAllEvents: DeleteAllEvents,
    val resetEventsTable: ResetEventsTable
)
