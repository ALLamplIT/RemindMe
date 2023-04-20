package com.lit.remindme.Notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.lit.remindme.MainActivity
import com.lit.remindme.R
import com.lit.remindme.feature_events.domain.model.RemindMeConstants
import java.util.*

class NotificationService(private val context: Context): NotificationServiceInterface {
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override fun sendNotification(inDays: Int, name: String, date: String, anniversary: String, eventId: Int) {

        Log.d("DBG-NotificationService","$name $eventId")

        val uniqueInt = Random().nextInt(0xFFFFFFF);
        val remindMeMainScreenIntent = Intent(context, MainActivity::class.java)
            .putExtra("eventId",eventId)

        val remindMeMainScreenPendingIntent = PendingIntent.getActivity(
            context,
            uniqueInt,
            remindMeMainScreenIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )
        val inDaysText = when (inDays) {
            0 -> context.getString(R.string.string_main_notification_today_text)
            1 -> context.getString(R.string.string_main_notification_tomorrow_text)
            else -> context.getString(R.string.string_main_notification_in_days_text).format(inDays)
        }
        val notificationText = context.getString(R.string.string_main_notification_template)
            .format(inDaysText, name, date, anniversary)

        val notification = NotificationCompat.Builder(
            context,
            RemindMeConstants.EVENT_MAIN_NOTIFICATION_CHANNEL
        )
            .setSmallIcon(R.drawable.ic_baseline_circle_notifications_24)
            .setContentTitle(context.getString(R.string.string_main_notification_channel_title))
            .setContentText(notificationText)
            .setContentIntent(remindMeMainScreenPendingIntent)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(eventId, notification)
    }
}