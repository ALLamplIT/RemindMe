package com.lit.remindme.feature_events.presentation.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class PermissionsCheck {
    fun hasContactsPermission(context: Context) =
        ActivityCompat.checkSelfPermission(context,
            Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
}