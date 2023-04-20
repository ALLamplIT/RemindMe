package com.lit.remindme.feature_events.data.data_sources

import android.content.Context
import com.google.gson.Gson
import com.lit.remindme.R
import com.lit.remindme.feature_events.domain.model.Settings
import java.io.File
import javax.inject.Inject

class SettingsStore @Inject constructor(private val context: Context) {

    fun writeSettings(settings:Settings): Unit {
        val fileOut = File(context.filesDir, context.getString(R.string.string_app_settings_file_name))
        val jsonOut = Gson().toJson(settings)
        fileOut.writeText(jsonOut)
    }

    fun readSettings(): Settings {
        val fileIn = File(context.filesDir, context.getString(R.string.string_app_settings_file_name))
        if (!fileIn.exists()) {
            return Settings()
        }
        val json = fileIn.readText()

        return Gson().fromJson(json, Settings::class.java)
    }
}