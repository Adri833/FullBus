package es.thatapps.fullbus.presentation.components

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


@Composable
fun SuggestionDialog(
    onDismiss: () -> Unit,
    onSend: (String) -> Unit
) {
    val context = LocalContext.current
    var suggestionText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = "Icono sugerencia",
                    tint = Color(0xFF2C9421),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Enviar sugerencia",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column {
                TextField(
                    value = suggestionText,
                    onValueChange = {
                        suggestionText = it
                        errorMessage = "" // Limpia el error al escribir
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Deja tu sugerencia...", fontSize = 16.sp) },
                    textStyle = TextStyle(fontSize = 18.sp),
                    isError = errorMessage.isNotEmpty()
                )
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontSize = 15.sp
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (suggestionText.isBlank()) {
                        errorMessage = "La sugerencia no puede estar vacía"
                        return@Button
                    }
                    saveSuggestionToFirestore(suggestionText, context, onDismiss)
                },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C9421))
            ) {
                Text(text = "Enviar", fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C))
            ) {
                Text("Cancelar", fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
            }
        }
    )

}

fun saveSuggestionToFirestore(
    suggestion: String,
    context: android.content.Context,
    onDismiss: () -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    // Verifica que el usuario esté autenticado
    val userEmail = currentUser?.email.orEmpty()

    if (userEmail.isNotEmpty()) {
        val userRef = db.collection("sugerencias").document(userEmail)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(userRef)

            // Obtiene el número actual o empieza desde 1
            val currentCount = snapshot.getLong("count") ?: 0
            val newCount = currentCount + 1

            // Crea el nuevo documento con el Gmail y el número
            val suggestionMap = hashMapOf(
                "text" to suggestion,
                "count" to newCount
            )

            transaction.set(userRef, mapOf("count" to newCount), SetOptions.merge())
            transaction.set(userRef.collection("entries").document(newCount.toString()), suggestionMap)
        }.addOnSuccessListener {
            Toast.makeText(context, "Gracias por tu sugerencia", Toast.LENGTH_SHORT).show()
            onDismiss()
        }.addOnFailureListener { e ->
            Toast.makeText(context, "Error al enviar sugerencia: $e", Toast.LENGTH_LONG).show()
        }
    } else {
        Toast.makeText(context, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
    }
}