package com.lit.remindme.feature_events.data.data_sources

import androidx.room.*
import com.lit.remindme.feature_events.domain.model.Event
import kotlinx.coroutines.flow.Flow

@Dao
interface InternalDataDao {

    @Query("SELECT * FROM event WHERE isVisible = 1")
    fun getEventsFlow(): Flow<List<Event>>

    @Query("SELECT * FROM event WHERE isVisible = 1")
    fun getEventsList(): List<Event>

    @Query("SELECT * FROM event WHERE SUBSTR(eventDate, 6, 5) = :monthAndDay AND eventDisabled != :eventStatus")
    fun getEventsFlowByMonthAndDay(monthAndDay: String, eventStatus: Boolean): Flow<List<Event>>

    @Query("SELECT * FROM event WHERE id = :id")
    suspend fun getEventByID(id: Int): Event?

    @Query("SELECT * FROM event WHERE lookupId = :lookupId")
    suspend fun getEventByLookupId(lookupId: String): Event?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event)

    @Delete
    suspend fun deleteEvent(event: Event)

    @Query("DELETE FROM event")
    fun deleteAllEvents()

    @Query("DELETE FROM event")
    fun resetEventsTable()
}