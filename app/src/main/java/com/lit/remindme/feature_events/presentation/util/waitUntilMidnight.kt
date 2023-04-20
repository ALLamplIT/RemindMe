package com.lit.remindme.feature_events.presentation.util

import android.util.Log
import com.lit.remindme.feature_events.domain.model.RemindMeConstants
import kotlinx.coroutines.delay
import java.time.LocalTime

suspend fun waitUntilMidnight(): Boolean{
    val now = LocalTime.now()
    val alarmTime = LocalTime.parse(RemindMeConstants.UPDATE_HIGHIGHTED_DAYS_TIME)
    val secondsDiff = alarmTime.toSecondOfDay() - now.toSecondOfDay()
    val isWait = secondsDiff < 0

    if(isWait) {
        val secondsToWait = RemindMeConstants.ONE_DAY_IN_SECONDS - now.toSecondOfDay() + 1
        delay((secondsToWait * 1000).toLong())
    }
    else
        delay(1000)
    return isWait
}
