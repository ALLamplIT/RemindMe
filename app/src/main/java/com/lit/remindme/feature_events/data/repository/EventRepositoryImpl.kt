package com.lit.remindme.feature_events.data.repository

import com.lit.remindme.feature_events.data.data_sources.InternalDataDao
import com.lit.remindme.feature_events.domain.model.Event
import com.lit.remindme.feature_events.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow

class EventRepositoryImpl(
    private val dao: InternalDataDao
): EventRepository {
    override fun getEventsFlow(): Flow<List<Event>> {
        return dao.getEventsFlow()
    }

    override suspend fun getEventsList(): List<Event> {
        return dao.getEventsList()
    }

    override fun getEventsFlowByMonthAndDay(monthAndDay: String, eventStatus: Boolean): Flow<List<Event>> {
        return dao.getEventsFlowByMonthAndDay(monthAndDay, eventStatus)
    }

    override suspend fun getEventById(id: Int): Event? {
        return dao.getEventByID(id)
    }

    override suspend fun getEventByLookupId(lookupId: String): Event? {
        return dao.getEventByLookupId(lookupId)
    }

    override suspend fun insertEvent(event: Event) {
        dao.insertEvent(event)
    }

    override suspend fun disableEvent(event: Event) {
        dao.deleteEvent(event)
    }

    override suspend fun deleteEvent(event: Event) {
        dao.deleteEvent(event)
    }

    override suspend fun deleteAllEvents() {
        dao.deleteAllEvents()
    }

    override suspend fun resetEventsTable() {
        dao.resetEventsTable()
    }
}