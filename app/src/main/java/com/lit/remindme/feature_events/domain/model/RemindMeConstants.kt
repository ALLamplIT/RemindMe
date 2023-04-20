package com.lit.remindme.feature_events.domain.model

object RemindMeConstants {
    const val REQUEST_PERMISSIONS_REQUEST_CODE = 0
    const val WORKER_INTERVAL_IN_MINUTES = 60
    const val ONE_DAY_IN_SECONDS = 24 * 60 * 60
    const val WORKER_INTERVAL_IN_SECONDS = WORKER_INTERVAL_IN_MINUTES * 60
    const val WORKER_INTERVAL_IN_MILLIS = WORKER_INTERVAL_IN_SECONDS * 1000
    const val WORKER_15_MINUTES_IN_SECONDS = 15 * 60
    const val WORKER_30_MINUTES_IN_SECONDS = 30 * 60
    const val WORKER_60_MINUTES_IN_SECONDS = 60 * 60
    const val WORKER_ALARM_TIME = "11:45:00"
    const val UPDATE_HIGHIGHTED_DAYS_TIME = "00:00:01"
    const val NOTIFICATION_ALARM_ID = 1111
    const val EVENT_MAIN_NOTIFICATION_CHANNEL = "remindme_main"
    const val PERIODIC_WORKER_NAME = "event_worker"
    const val APP_VERSION = "0.16"
}