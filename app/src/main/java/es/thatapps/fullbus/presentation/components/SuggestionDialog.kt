package es.thatapps.fullbus.presentation.components

import android.widget.Toast
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun SuggestionDialog(
    onDismiss: () -> Unit,
    onSend: (String) -> Unit
) {
    val context = LocalContext.current // Obtiene el contexto actual
    var suggestionText by remember { mutableStateOf(TextFieldValue("")) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Enviar sugerencia") },
        text = {
            TextField(
                value = suggestionText,
                onValueChange = { suggestionText = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Deja tu sugerencia...") }
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    // Manejo de excepciones
                    if (suggestionText == null) {

                    }

                    // Guarda la sugerencia en Firestore
                    val db = FirebaseFirestore.getInstance()
                    val suggestion = hashMapOf(
                        "text" to suggestionText.text
                    )

                    db.collection("sugerencias")
                        .add(suggestion)
                        .addOnSuccessListener { documentReference ->
                            Toast.makeText(context, "Gracias por tu sugerencia", Toast.LENGTH_SHORT).show()
                            suggestionText = TextFieldValue("") // Limpia el campo
                            onDismiss() // Cierra el diálogo
                        }
                        .addOnFailureListener { e ->
                            println("Error añadiendo sugerencia: $e")
                        }
                },

                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2c9421))
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