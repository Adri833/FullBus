package es.thatapps.fullbus.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class AuthRepository @Inject constructor(private val firebaseAuth: FirebaseAuth) {

    suspend fun register(email: String, password: String): Result<String> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Result.success(result.user?.uid ?: "No user ID")
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error registering user: ${e.message}") // Manejo de errores
            Result.failure(e)
        }
    }
}
