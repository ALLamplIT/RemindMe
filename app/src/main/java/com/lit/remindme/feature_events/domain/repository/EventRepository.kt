package com.lit.remindme.feature_events.domain.repository

import com.lit.remindme.feature_events.domain.model.Event
import com.lit.remindme.feature_events.domain.model.EventDomain
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun getEvents(): Flow<List<Event>>

    fun getEventsByMonthAndDay(monthAndDay: String, eventStatus: Boolean): Flow<List<Event>>

    suspend fun getEventById(id: Int): Event?

    suspend fun getEventByLookupId(lookupId: String): Event?

    suspend fun insertEvent(event: Event)

    suspend fun deleteEvent(event: Event)

    suspend fun disableEvent(event: Event)

    suspend fun deleteAllEvents()

    suspend fun resetEventsTable()
}