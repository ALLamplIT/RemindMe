package com.lit.remindme.feature_events.presentation.events

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lit.remindme.Notifications.NotificationWorkerStarter
import com.lit.remindme.feature_events.data.data_sources.SettingsStore
import com.lit.remindme.feature_events.data.data_sources.SyncDeviceContacts
import com.lit.remindme.feature_events.domain.model.Event
import com.lit.remindme.feature_events.domain.model.Settings
import com.lit.remindme.feature_events.domain.repository.EventRepository
import com.lit.remindme.feature_events.domain.use_case.EventUseCases
import com.lit.remindme.feature_events.util.EventsOrderType
import com.lit.remindme.feature_events.util.OrderDirection
import com.lit.remindme.R
import com.lit.remindme.feature_events.domain.model.EventDomain
import com.lit.remindme.feature_events.domain.util.getItemColor
import com.lit.remindme.feature_events.presentation.util.waitUntilMidnight
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val eventUseCases: EventUseCases,
//    private val context: Context,
    private val repository: EventRepository,
    private val settingsStore: SettingsStore,
    private val notificationWorkerStarter: NotificationWorkerStarter,
    private val syncDeviceContacts: SyncDeviceContacts

) : ViewModel() {
    private val _state = mutableStateOf(EventsState())
    val state: State<EventsState> = _state

    private val _eventsList = mutableStateListOf<EventDomain>()
    val eventsList: SnapshotStateList<EventDomain> = _eventsList

    private var recentlyDeletedEvent: EventDomain? = null

    private var privateGetEventsJob: Job? = null

    init {
//        Log.d("DBG-EventsViewModel", "$context::class.java")
        val settingsFromFile = settingsStore.readSettings()
        _state.value = state.value.copy(
            dailyReminderTime = settingsFromFile.dailyRemindTime
        )
        _state.value = state.value.copy(
            eventsOrderType = EventsOrderType.Date(OrderDirection.Ascending)
        )

        viewModelScope.launch {
//            delay(100) //Init delay for state
            updateHighlightedEvents() //Init when App starts
        }

        val updateHighlightedDays = CoroutineScope(Dispatchers.IO)
        updateHighlightedDays.launch {
            while (true) {
                waitUntilMidnight()
                updateHighlightedEvents() //Update if app still runs and date changed
            }
        }
    }

    private suspend fun updateHighlightedEvents() {
        var syncLoop = true

        while (syncLoop) {
            if (eventsList.isEmpty())
                delay(1000)
            syncDeviceContacts.doSync()
            privateGetEvents(state.value.eventsOrderType)
            syncLoop = eventsList.isEmpty()
        }

//        val now = LocalDate.now()
//        eventsList.forEachIndexed() { index, thisEvent ->
//            val newColorId = getItemColor(
//                index,
//                now,
//                LocalDate.parse(thisEvent.eventDate).withYear(now.year)
//            )
//            if (thisEvent.ColorId != newColorId) {
//                _eventsList[index] = thisEvent.copy(ColorId = newColorId)
//            }
//        }
    }

    fun onEvent(event: EventsEvent) {
        when (event) {
            is EventsEvent.Order -> {
                if (state.value.eventsOrderType::class == event.eventOrder::class &&
                    state.value.eventsOrderType.orderDirection == event.eventOrder.orderDirection
                ) {
                    return
                }
                _state.value = state.value.copy(
                    eventsOrderType = event.eventOrder
                )
                privateGetEvents(state.value.eventsOrderType)
            }
            is EventsEvent.DeleteEvent -> {
                viewModelScope.launch {
                    recentlyDeletedEvent = event.event
                    _eventsList.remove(event.event)
                    eventUseCases.deleteEvent(event.event.toDb())
                }
            }
            is EventsEvent.DisableEvent -> {
                viewModelScope.launch {
                    val changedEvent = event.event.copy(eventDisabled = !event.event.eventDisabled)
                    _eventsList[event.index] = changedEvent
                    eventUseCases.addEvent(changedEvent.toDb())
                }
            }
            is EventsEvent.RestoreEvent -> {
                viewModelScope.launch {
                    eventUseCases.addEvent(recentlyDeletedEvent?.toDb() ?: return@launch)
                    recentlyDeletedEvent = null
                }
                privateGetEvents(state.value.eventsOrderType)
            }
            is EventsEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
            is EventsEvent.ChangeDailyRemindTime -> {
                val settings = Settings(
                    dailyRemindTime = event.newTime
                )

                settingsStore.writeSettings(settings)
                notificationWorkerStarter.start()

                _state.value = state.value.copy(
                    dailyReminderTime = event.newTime
                )
            }
        }
    }

    private fun privateGetEvents(eventsOrderType: EventsOrderType) {
        privateGetEventsJob?.cancel()
        privateGetEventsJob =
            eventUseCases.getEvents(eventsOrderType)
                .onEach { events ->
                    events.forEach { event ->
//                        Log.d("DBG-privateGetEvents", "#01 $event")
                        _eventsList.removeIf {
                            it.id == event.id
                        }
                        _eventsList.add(event.toDomain())
                    }
                }.launchIn(viewModelScope)
    }
}