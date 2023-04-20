package com.lit.remindme.Notifications

interface NotificationServiceInterface {

    fun sendNotification(inDays: Int, name: String, date: String, anniversary: String, id: Int): Unit

}