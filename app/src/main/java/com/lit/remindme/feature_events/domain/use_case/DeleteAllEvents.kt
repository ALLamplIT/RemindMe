package com.lit.remindme.feature_events.domain.use_case

import com.lit.remindme.feature_events.domain.model.Event
import com.lit.remindme.feature_events.domain.repository.EventRepository

class DeleteAllEvents(
    private val repository: EventRepository
) {
    suspend operator fun invoke() {
        repository.deleteAllEvents()
    }
}