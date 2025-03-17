package es.thatapps.fullbus.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import es.thatapps.fullbus.R
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

@Composable
fun imagePickerLauncher(
    onImageSelected: (Uri?) -> Unit
): ManagedActivityResultLauncher<String, Uri?> = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
) { uri: Uri? ->
    onImageSelected(uri)
}

fun encodeImageToBase64(
    context: Context,
    uri: Uri,
    quality: Int = 70,
    maxWidth: Int = 800,
    maxHeight: Int = 800,
): String? = try {
    val inputStream = context.contentResolver.openInputStream(uri)
    val bitmap = BitmapFactory.decodeStream(inputStream)

    val compressedBitmap = bitmap?.let { scaleBitmap(it, maxWidth, maxHeight) }
    val outputStream = ByteArrayOutputStream()
    compressedBitmap?.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)

    val compressedBytes = outputStream.toByteArray()
    Base64.encodeToString(compressedBytes, Base64.DEFAULT)

} catch (e: Exception) {
    e.printStackTrace()
    null
}

fun getImageFromBase64(base64: String?): ImageBitmap? = base64?.let {
    decodeBase64ToBitmap(base64)?.asImageBitmap()
}

private fun decodeBase64ToBitmap(base64: String): Bitmap? = try {
    val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
    BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

} catch (e: Exception) {
    e.printStackTrace()
    null
}

private fun scaleBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
    val width = bitmap.width
    val height = bitmap.height

    val widthRatio = maxWidth.toFloat() / width
    val heightRatio = maxHeight.toFloat() / height
    val scaleFactor = minOf(widthRatio, heightRatio)

    if (scaleFactor >= 1) return bitmap

    val newWidth = (width * scaleFactor).toInt()
    val newHeight = (height * scaleFactor).toInt()

    return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
}

@Composable
fun ImageBase64(
    imageUrl: String?,
    size: Dp,
    padding: Dp? = null,
) {
    getImageFromBase64(imageUrl)?.let { imageUrlNotNull ->
        Image(
            bitmap = imageUrlNotNull,
            contentDescription = null,
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .padding(padding ?: 0.dp),
            contentScale = ContentScale.Crop
        )
    } ?: run {
        Image(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .padding(padding ?: 0.dp),
            painter = painterResource(id = R.drawable.default_pfp),
            contentDescription = null,
            contentScale = ContentScale.Inside
        )
    }
}

suspend fun getPFP(): String? {
    val userId = FirebaseAuth.getInstance().currentUser?.email ?: return null
    return try {
        val firestoreRef = FirebaseFirestore.getInstance()
            .collection("users")
            .document(userId)

        val snapshot = firestoreRef.get().await()
        snapshot.getString("PFP")
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}