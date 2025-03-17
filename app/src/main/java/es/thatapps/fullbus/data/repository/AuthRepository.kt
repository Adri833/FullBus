package es.thatapps.fullbus.data.repository

import android.content.Context
import android.net.Uri
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import es.thatapps.fullbus.R
import es.thatapps.fullbus.utils.encodeImageToBase64
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val context: Context,
) {
    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email,password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(email: String, password: String): Result<String> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Result.success(result.user?.uid ?: "No user ID")

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registerUserInFirestore(email: String) {
        try {
            val userDocument = firestore.collection("users").document(email).get().await()

            if (!userDocument.exists()) {
                val userData = mapOf(
                    "email" to email,
                    "username" to generateRandomUsername(),
                    "PFP" to getDefaultPFP(context)
                )

                firestore.collection("users").document(email).set(userData).await()
            }
        } catch (e: Exception) {
            throw Exception("Error al registrar el usuario en Firestore: ${e.message}", e)
        }
    }

    suspend fun signInWithGoogle(credential: AuthCredential): Result<AuthResult?> = try {
        val authResult = firebaseAuth.signInWithCredential(credential).await()
        authResult.user?.let {

            registerUserInFirestore(it.email ?: throw Exception("Correo electrónico no disponible"))
            Result.success(authResult)
        } ?: throw Exception("Usuario nulo")
    } catch (e: Exception) {
        Result.failure(e)
    }

    fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    fun logout() {
        firebaseAuth.signOut()
    }

    suspend fun deleteAccount(): Result<Unit> {
        val user = firebaseAuth.currentUser
        return if (user != null) {
            try {
                val userEmail = user.email ?: throw Exception("Email no disponible")
                firestore.collection("users").document(userEmail).delete().await()
                user.delete().await()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        } else {
            Result.failure(Exception("Usuario no registrado"))
        }
    }

    // Funcion para generar un nombre aleatorio para el usuario
    private fun generateRandomUsername(): String {
        val randomNumber = (1..9999).random()
        return "User$randomNumber"
    }

    private fun getDefaultPFP(context: Context): String {
        val uri = Uri.parse("android.resource://${context.packageName}/${R.drawable.default_pfp}")
        return encodeImageToBase64(context, uri) ?: ""
    }

    // Metodo para recoger el nombre de usuario actual
    suspend fun getUserName(): String {
        val currentUser = firebaseAuth.currentUser ?: throw Exception("Usuario no registrado")

        return try {
            val document = firestore.collection("users").document(currentUser.email!!).get().await()
            document.getString("username") ?: throw Exception("Username not found")
        } catch (e: Exception) {
            throw Exception("Error al obtener el username: ${e.message}", e)
        }
    }

    // Metodo para actualizar el nombre de usuario
    suspend fun updateUserName(email: String, newUsername: String) {
        try {
            val userRef = firestore.collection("users").document(email)
            userRef.update("username", newUsername).await()
        } catch (e: Exception) {
            throw Exception("Error al actualizar el username: ${e.message}", e)
        }
    }
}