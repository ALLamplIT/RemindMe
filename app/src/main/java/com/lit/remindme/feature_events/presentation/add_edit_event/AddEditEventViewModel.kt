package com.lit.remindme.feature_events.presentation.add_edit_event

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lit.remindme.R
import com.lit.remindme.feature_events.domain.model.Event
import com.lit.remindme.feature_events.domain.model.EventTypes
import com.lit.remindme.feature_events.domain.model.InvalidEventException
import com.lit.remindme.feature_events.domain.use_case.EventUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AddEditEventViewModel @Inject constructor(
    private val eventUseCases: EventUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var thumbURI: String = ""
    var lookupId: String = ""
    var eventType: EventTypes = EventTypes.EventFromUser

    private val _eventTitle = mutableStateOf(EventTextFieldState(
        text ="",
        hint = R.string.string_name_label
    ))
    val eventTitle: State<EventTextFieldState> = _eventTitle

    private val _eventEventDate = mutableStateOf(EventTextFieldState(
        text = "01.01.1900",
        hint = R.string.string_date_label
    ))
    val eventEventDate: State<EventTextFieldState> = _eventEventDate

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentEventId: Int? = null

    init {
        savedStateHandle.get<Int>("eventId")?.let { eventId ->
            Log.d("DBG-AddEditEventViewModel-Init","eventId: $eventId")
            if(eventId != -1) {
                viewModelScope.launch {
                    eventUseCases.getEventById(eventId)?.also { event ->
                        eventType = event.eventType
                        thumbURI = event.thumbUri
                        lookupId = event.lookupId
                        currentEventId = event.id
                        _eventTitle.value = eventTitle.value.copy(
                            text = event.title,
                            isHintVisible = false
                        )
                        _eventEventDate.value = _eventEventDate.value.copy(
                            text = DateTimeFormatter
                                .ofPattern("dd.MM.yyyy")
                                .format(LocalDate.parse(event.eventDate)),
                            isHintVisible = false
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditEventEvent) {
        when(event) {
            is AddEditEventEvent.EnteredTitle -> {
                _eventTitle.value = eventTitle.value.copy(
                    text = event.value
                )
            }
            is AddEditEventEvent.EnteredEventDate -> {
                _eventEventDate.value = eventEventDate.value.copy(
                    text = event.value
                )
            }
            is AddEditEventEvent.ChangeTitleFocus -> {
                _eventTitle.value = eventTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            eventTitle.value.text.isBlank()
                )
            }
            is AddEditEventEvent.ChangeEventDateFocus -> {
                _eventEventDate.value = eventEventDate.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            eventEventDate.value.text.isBlank()
                )
            }
            is AddEditEventEvent.SaveEvent -> {
                viewModelScope.launch {
                    try {
                        eventUseCases.addEvent(
                            Event(
                                title = eventTitle.value.text,
                                displayName = "",
                                eventDate = LocalDate.parse(
                                    eventEventDate.value.text,
                                    DateTimeFormatter
                                        .ofPattern("dd.MM.yyyy")).toString(),
                                lookupId = "",
                                eventType = EventTypes.EventFromUser,
                                eventDisabled = false,
                                isVisible = true,
                                id = currentEventId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveEvent)
                    } catch(e: InvalidEventException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message,
                                resourceID = R.string.string_err_msg_unable_to_save
                            )
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String?, val resourceID: Int): UiEvent()
        object SaveEvent: UiEvent()
    }
}