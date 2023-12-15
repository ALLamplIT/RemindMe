package com.lit.remindme.feature_events.domain.model

data class EventDomain constructor(
    val lookupId: String  = "",
    val title: String = "",
    val displayName: String = "",
    val eventDate: String = "",
    val eventType: EventTypes = EventTypes.EventFromContacts,
    val thumbUri: String = "",
    val eventDisabled: Boolean = false,
    val isVisible: Boolean = true,
//    val ColorId: Int = R.color.row_background_1,
    val id: Int? = null
){
    fun toDb() :Event {
        return Event(lookupId = lookupId,
            title = title,
            displayName = displayName,
            eventDate = eventDate,
            eventType = eventType,
            thumbUri = thumbUri,
            eventDisabled = eventDisabled,
            isVisible = isVisible,
            id = id)
    }
}