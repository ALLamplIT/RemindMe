package com.lit.remindme.feature_events.presentation.util

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.lit.remindme.feature_events.domain.model.Event
import java.io.FileNotFoundException
import java.io.IOException

@Composable
fun GetContactImagePainter(context: Context, thumbUri: String): ImageBitmap? {
    val eventThumbURIColumn = Uri.parse(thumbUri)
    var assetFD: AssetFileDescriptor? = null
    var bitmap: ImageBitmap? = null

    if(PermissionsCheck().hasContactsPermission(context)) {
        try {
            assetFD = context.contentResolver?.openAssetFileDescriptor(eventThumbURIColumn, "r")
            bitmap = assetFD?.fileDescriptor?.let { fileDescriptor ->
                BitmapFactory.decodeFileDescriptor(fileDescriptor, null, null).asImageBitmap()
            }
        } catch (e: FileNotFoundException) {
//        println(e)
        } finally {
            try {
                assetFD?.close()
            } catch (e: IOException) {
//            println(e)
            }
        }
    }
    return bitmap
}