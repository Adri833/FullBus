package es.thatapps.fullbus.presentation.components

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


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
        title = { Text("Enviar sugerencia") },
        text = {
            Column {
                TextField(
                    value = suggestionText,
                    onValueChange = {
                        suggestionText = it
                        errorMessage = "" // Limpia el error al escribir
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Deja tu sugerencia...") },
                    isError = errorMessage.isNotEmpty()
                )
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontSize = 12.sp
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
                Text(text = "Enviar", fontSize = 16.sp)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Cancelar", fontSize = 16.sp)
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
        // Mapa con los datos de la sugerencia
        val suggestionMap = hashMapOf(
            "text" to suggestion,
            "userId" to currentUser?.uid,
            "userEmail" to userEmail
        )

        // Usa el email como ID del documento
        db.collection("sugerencias")
            .document(userEmail)
            .set(suggestionMap)
            .addOnSuccessListener {
                Toast.makeText(context, "Gracias por tu sugerencia", Toast.LENGTH_SHORT).show()
                onDismiss()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error al enviar sugerencia: $e", Toast.LENGTH_LONG).show()
            }
    } else {
        Toast.makeText(context, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
    }
}

@Preview(showBackground = true)
@Composable
fun SuggestionDialogPreview() {
    SuggestionDialog(
        onDismiss = { /* Acción al cerrar */ },
        onSend = { suggestion ->
            // Aquí puedes manejar lo que sucede con la sugerencia
            println("Sugerencia enviada: $suggestion")
        }
    )
}