package com.lit.remindme

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.lit.remindme.feature_events.domain.model.RemindMeConstants
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EventApp: Application(){

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val mainChannel = NotificationChannel(
                RemindMeConstants.EVENT_MAIN_NOTIFICATION_CHANNEL,
                applicationContext.getString(R.string.string_main_notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            mainChannel.description = applicationContext.getString(R.string.string_main_notification_channel_description)

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(mainChannel)
        }
    }
}