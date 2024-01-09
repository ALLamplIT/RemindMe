package com.lit.remindme.Notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.lit.remindme.feature_events.data.data_sources.SettingsStore
import com.lit.remindme.feature_events.domain.broadcast_receiver.NotificationAlarmReceiver
import com.lit.remindme.feature_events.domain.model.RemindMeConstants
import kotlinx.coroutines.*
import java.time.*
import java.time.format.DateTimeFormatter

class NotificationWorker(
    private val context: Context,
    private val workerParams: WorkerParameters,
): CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("DBG-NotificationWorker doWork","Started")
        val alarmManager = context.getSystemService(AlarmManager::class.java)
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
        val todayDate = LocalDate.now()
        val todayDateString = DateTimeFormatter
            .ofPattern("dd.MM.yyyy")
            .format(todayDate)
        val settingsFromFile = SettingsStore(context).readSettings()
        var alarmTime = LocalDateTime.parse("$todayDateString ${settingsFromFile.dailyRemindTime}:00",formatter)

        if(alarmTime.isBefore(LocalDateTime.now())) {
            alarmTime = alarmTime.plusDays(1)
        }

        Log.d("DBG-NotificationWorker doWork","Set Alarm to:" + DateTimeFormatter
            .ofPattern("dd.MM.yyyy HH:mm:ss")
            .format(alarmTime)
        )

        val notificationAlarmIntent = Intent(context, NotificationAlarmReceiver::class.java)

        val alarm = alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmTime.atZone(ZoneId.systemDefault()).toEpochSecond()*1000,
            PendingIntent.getBroadcast(
                context,
                RemindMeConstants.NOTIFICATION_ALARM_ID,
                notificationAlarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        )

        return Result.success(
            workDataOf(NotificationWorkerKeys.ERROR_MSG to "NoError")
        )
    }

}