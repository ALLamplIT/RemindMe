package com.lit.remindme.feature_events.domain.use_case

data class EventUseCases(
    val getEventsFlow: GetEventsFlow,
    val getEventsFlowByMonthAndDay: GetEventsFlowByMonthAndDay,
    val deleteEvent: DeleteEvent,
    val disableEvent: DisableEvent,
    val addEvent: AddEvent,
    val getEventById: GetEventById,
    val getEventByLookupId: GetEventByLookupId,
    val deleteAllEvents: DeleteAllEvents,
    val resetEventsTable: ResetEventsTable
)
