package com.lit.remindme.feature_events.domain.broadcast_receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.lit.remindme.Notifications.NotificationService
import com.lit.remindme.feature_events.data.data_sources.SyncDeviceContacts
import com.lit.remindme.feature_events.domain.model.Event
import com.lit.remindme.feature_events.domain.repository.EventRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class NotificationAlarmReceiver:BroadcastReceiver() {
    @Inject
    lateinit var repository: EventRepository

    override fun onReceive(context: Context?, intent: Intent?) {
//        Log.d("DBG-NotificationAlarmReceiver", "started")
        if (intent == null || context == null)
            return
        doSyncWithContacts(context, repository)
    }

    private fun doSyncWithContacts(context: Context, repository: EventRepository) {
//        Log.d("DBG-doSyncWithContacts", "started")
        val today = LocalDate.now()
        val notificationService = NotificationService(context)
        val syncScope = CoroutineScope(Dispatchers.Default)
        syncScope.launch {
            val syncDeviceContacts = SyncDeviceContacts(context, repository)
            syncDeviceContacts.doSync()
        }

        val dayBeforeScope = CoroutineScope(Dispatchers.Default)
        val todayScope = CoroutineScope(Dispatchers.Default)
        dayBeforeScope.launch {
            doNotifications(repository, today, 1, notificationService)
        }
        todayScope.launch {
            doNotifications(repository, today, 0, notificationService)
        }
    }

    private suspend fun doNotifications(
        repository: EventRepository,
        today: LocalDate,
        daysInAdvance: Int,
        notificationService: NotificationService
    ) {
//        Log.d("DBG-doNotifications", "Started")

        val eventFlow = getNotificationEvents(
            repository,
            today,
            daysInAdvance
        )

        eventFlow
            .collect() { eventList ->
//                Log.d("DBG-doNotifications", "list:$eventList")
                eventList
                    .forEach { event ->
                        val eventDate = LocalDate.parse(event.eventDate)

//                        Log.d("DBG-doNotifications", "eventId ${event.id}")
                        notificationService.sendNotification(
                            daysInAdvance,
                            event.title,
                            DateTimeFormatter
                                .ofPattern("dd.MM.yyyy")
                                .format(eventDate),
                            (today.year - eventDate.year).toString(),
                            event.id ?: 0
                        )
                    }
            }
    }

    private fun getNotificationEvents(
        repository: EventRepository,
        today: LocalDate,
        daysInAdvance: Int
    ): Flow<List<Event>> {

        val searchDateString = DateTimeFormatter
            .ofPattern("MM-dd")
            .format(today.plusDays(daysInAdvance.toLong()))
        val eventFlow = repository.getEventsByMonthAndDay(
            searchDateString,
            true
        )

        return eventFlow.map { events ->
            events.sortedBy { event ->
                event.title
            }
        }.distinctUntilChanged()
    }
}