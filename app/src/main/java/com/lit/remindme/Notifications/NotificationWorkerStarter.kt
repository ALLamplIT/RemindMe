package com.lit.remindme.Notifications

import android.content.Context
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.lit.remindme.feature_events.domain.model.RemindMeConstants
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class NotificationWorkerStarter @Inject constructor(private val context: Context) {

    fun start() :Unit {
//    Log.d("DBG-notificationWorkerStarter","started")
        val notificationRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            RemindMeConstants.WORKER_INTERVAL_IN_MINUTES.toLong(), TimeUnit.MINUTES
        )
            .build()
        val workManager = WorkManager.getInstance(context)

        workManager
            .enqueueUniquePeriodicWork(
                RemindMeConstants.PERIODIC_WORKER_NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                notificationRequest
            )
//    Log.d("DBG-notificationWorkerStarter","finished")
    }
}