package com.lit.remindme.feature_events.domain.use_case

import com.lit.remindme.feature_events.domain.model.Event
import com.lit.remindme.feature_events.domain.model.EventDomain
import com.lit.remindme.feature_events.domain.repository.EventRepository

class GetEventById(
    private val repository: EventRepository
) {

    suspend operator fun invoke(id: Int): EventDomain? {
        return repository.getEventById(id)?.toDomain()
    }
}