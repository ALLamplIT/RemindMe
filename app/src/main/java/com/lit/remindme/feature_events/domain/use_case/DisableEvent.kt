package com.lit.remindme.feature_events.domain.use_case

import com.lit.remindme.feature_events.domain.model.Event
import com.lit.remindme.feature_events.domain.repository.EventRepository

class DisableEvent(
    private val repository: EventRepository
) {

    suspend operator fun invoke(event: Event) {
        repository.disableEvent(event)
    }
}