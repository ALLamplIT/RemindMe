package com.lit.remindme.feature_events.util

sealed class OrderDirection{
    object Ascending: OrderDirection()
    object Descending: OrderDirection()
}
