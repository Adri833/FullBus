package es.thatapps.fullbus.presentation.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _userBase64Image: MutableStateFlow<String?> = MutableStateFlow(null)
    val userBase64Image: StateFlow<String?> = _userBase64Image

    // Función para obtener la imagen base64 del usuario desde Firestore
    fun loadUserProfileImage(email: String) {
        viewModelScope.launch {
            firestore.collection("users")
                .document(email)
                .get()
                .addOnSuccessListener { document ->
                    _userBase64Image.value = document.getString("PFP") // El campo que almacena la imagen base64
                }
        }
    }
}
