package com.lit.remindme.feature_events.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
*/

@Entity
data class Event constructor(
    var lookupId: String,
    var title: String,
    var displayName: String,
    var eventDate: String,
    var eventType: EventTypes,
    @ColumnInfo(name = "thumbUri", defaultValue = "")
    var thumbUri: String = "",
    @ColumnInfo(name = "eventDisabled", defaultValue = "0")
    var eventDisabled: Boolean = false,
    @ColumnInfo(name = "isVisible", defaultValue = "1")
    var isVisible: Boolean = true,
    @PrimaryKey var id: Int? = null
){
    constructor():this(
        lookupId = "",
        title = "",
        displayName = "",
        eventDate = "",
        eventType = EventTypes.EventFromContacts,
        thumbUri = "",
        eventDisabled = false,
        isVisible = true,
        id = null
    )

    fun toDomain() :EventDomain {
        return EventDomain(lookupId = lookupId,
            title = title,
            displayName = displayName,
            eventDate = eventDate,
            eventType = eventType,
            thumbUri = thumbUri,
            eventDisabled = eventDisabled,
            isVisible = isVisible,
//            ColorId = R.color.row_background_1,
            id = id)
    }
}

class InvalidEventException(message: String): Exception(message)