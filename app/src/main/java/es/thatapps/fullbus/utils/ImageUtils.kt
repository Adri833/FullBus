package es.thatapps.fullbus.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import android.util.Base64

fun base64toImage(base64String: String): ImageBitmap? {
    return try {
        val decodedString = Base64.decode(base64String, Base64.NO_WRAP)
        val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        bitmap?.asImageBitmap()
    } catch (e: Exception) {
        null
    }
}
