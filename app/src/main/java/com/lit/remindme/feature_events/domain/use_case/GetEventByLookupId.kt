package com.lit.remindme.feature_events.domain.use_case

import com.lit.remindme.feature_events.domain.model.Event
import com.lit.remindme.feature_events.domain.model.EventDomain
import com.lit.remindme.feature_events.domain.repository.EventRepository

class GetEventByLookupId(
    private val repository: EventRepository
) {

    suspend operator fun invoke(lookupId: String): EventDomain? {
        return repository.getEventByLookupId(lookupId)?.toDomain()
    }
}