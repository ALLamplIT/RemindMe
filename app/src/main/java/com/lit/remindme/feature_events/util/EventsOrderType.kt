package com.lit.remindme.feature_events.util

sealed class EventsOrderType(val orderDirection: OrderDirection) {
    class Title(orderDirection: OrderDirection): EventsOrderType(orderDirection)
    class Date(orderDirection: OrderDirection): EventsOrderType(orderDirection)
    class Type(orderDirection: OrderDirection): EventsOrderType(orderDirection)

    fun copy(orderDirection: OrderDirection): EventsOrderType{
        return when(this){
            is Title -> Title(orderDirection)
            is Date -> Date(orderDirection)
            is Type -> Type(orderDirection)
        }
    }
}
