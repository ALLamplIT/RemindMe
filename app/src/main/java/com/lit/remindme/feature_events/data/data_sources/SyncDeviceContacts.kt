package com.lit.remindme.feature_events.data.data_sources

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import com.lit.remindme.feature_events.domain.model.Event
import com.lit.remindme.feature_events.domain.model.EventTypes
import com.lit.remindme.feature_events.domain.repository.EventRepository
import com.lit.remindme.feature_events.presentation.util.PermissionsCheck
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class SyncDeviceContacts @Inject constructor(private val context: Context, private val repository: EventRepository) {

    init {
        PermissionsCheck().hasContactsPermission(context)
    }

    fun doSync() {
        if (PermissionsCheck().hasContactsPermission(context))
            privateAddEvents()
    }

    private fun privateAddEvents() {
        val cursorContactsEntries: Cursor? = getContactsEntries()
        if (cursorContactsEntries != null) {
            val dayColumn =
                cursorContactsEntries.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE)
            val nameColumn =
                cursorContactsEntries.getColumnIndex(ContactsContract.CommonDataKinds.Event.DISPLAY_NAME)
            val lookupKeyColumn =
                cursorContactsEntries.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY)
            val photoThumbURIColumn =
                cursorContactsEntries.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)
            var eventDate: LocalDate
            val syncScope = CoroutineScope(Dispatchers.Default)

            syncScope.launch {
                withContext(Dispatchers.IO) {
//                eventUseCases.deleteAllEvents()
                    while (cursorContactsEntries.moveToNext()) {
                        val eventLookupKey =
                            cursorContactsEntries.getString(lookupKeyColumn) ?: ""
                        val eventThumbURIColumn =
                            Uri.parse(cursorContactsEntries.getString(photoThumbURIColumn) ?: "")
                        val eventDayString = cursorContactsEntries.getString(dayColumn) ?: ""

                        eventDate = try {
                            LocalDate.parse(eventDayString)
                        } catch (e: Exception) {
//                println("Exception $e")
                            LocalDate.parse("1900-01-01")
                        }
                        val eventName = cursorContactsEntries.getString(nameColumn) ?: "-na-"
                        val prevEvent = repository.getEventByLookupId(eventLookupKey)
                        val newEventId: Int? = if (prevEvent is Event) prevEvent.id else null
                        val newEventDisabled: Boolean =
                            if (prevEvent is Event) prevEvent.eventDisabled else false
                        val newEvent: Event = Event(
                            lookupId = eventLookupKey,
                            title = eventName,
                            displayName = eventDate.toString(),
                            eventDate = eventDate.toString(),
                            eventType = EventTypes.EventFromContacts,
                            thumbUri = eventThumbURIColumn.toString(),
                            eventDisabled = newEventDisabled,
                            isVisible = true,
                            id = newEventId
                        )
//                        if (prevEvent is Event) {
//                            Log.d("DBG-privateAddEvents","#01 Name: $eventName Day: $eventDate LookupID:$eventLookupKey PrevEntry:$prevEvent URI:$eventThumbURIColumn")
//                        } else {
//                            Log.d("DBG-privateAddEvents","#02 Name: $eventName Day: $eventDate New Entry")
//                        }
                        repository.insertEvent(newEvent)
                    }
                    cursorContactsEntries.close()
                }
                doRemoveDoublesFromRoomDB()
            }
        }
    }

    private fun getContactsEntries(): Cursor? {
        val uri = ContactsContract.Data.CONTENT_URI
        val projection = arrayOf(
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Event.CONTACT_ID,
            ContactsContract.CommonDataKinds.Event.START_DATE,
            ContactsContract.Contacts.LOOKUP_KEY,
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI
        )
        val where = ContactsContract.Data.MIMETYPE + "= ? AND " +
                ContactsContract.CommonDataKinds.Event.TYPE + "=" +
                ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY
        val selectionArgs = arrayOf(
            ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE
        )
        val sortOrder: String? = null
        return context.contentResolver.query(uri, projection, where, selectionArgs, sortOrder)
    }

    private suspend fun doRemoveDoublesFromRoomDB() {
        Log.d("DBG-doRoom","enter")

        repository.getEvents().collect { events ->
            Log.d("DBG-doRoom","${events.size}")
            events.forEach  { event  ->
                Log.d("DBG-doRoom","${event.lookupId}")
            }
        }
/*
        val diff = roomDBLookupIds.subtract(contactsDBLookupIds.toSet())

        diff.forEach { lookupId ->
            Log.d("DBG-doRoom","id:$lookupId")
            val event = repository.getEventByLookupId(lookupId)
            Log.d("DBG-doRoom","event:${event.toString()}")
            if (event != null && event.eventType != EventTypes.EventFromUser) {
                repository.deleteEvent(event)
                Log.d("DBG-doRoom","del")
            }
        }*/
    }
}