package com.lit.remindme.feature_events.domain.model

import kotlinx.serialization.Serializable

data class Settings(
    val dailyRemindTime: String = "11:00"
){

}