package com.lit.remindme.feature_events.domain.use_case

import android.util.Log
import com.lit.remindme.R
import com.lit.remindme.feature_events.domain.model.Event
import com.lit.remindme.feature_events.domain.model.InvalidEventException
import com.lit.remindme.feature_events.domain.repository.EventRepository

class AddEvent(
    private val repository: EventRepository
) {
    @Throws(InvalidEventException::class)
    suspend operator fun invoke(event: Event) {
        if(event.title.isBlank()) {
            throw InvalidEventException("#ErrSR:"+ R.string.string_err_event_needs_a_title)
        }
        repository.insertEvent(event)
    }
}