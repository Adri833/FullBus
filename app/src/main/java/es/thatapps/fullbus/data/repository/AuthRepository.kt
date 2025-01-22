package es.thatapps.fullbus.data.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) {

    suspend fun register(email: String, password: String): Result<String> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Result.success(result.user?.uid ?: "No user ID")

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signInWithGoogle(credential: AuthCredential): Result<AuthResult?> = try {
        val authResult = firebaseAuth.signInWithCredential(credential).await()
        authResult.user?.let {
            Result.success(authResult)
//            setRandomNameToNewUser(authResult)
        } ?: throw Exception("Usuario nulo")
    } catch (e: Exception) {
        Result.failure(e)
    }

//    private suspend fun setRandomNameToNewUser(result: AuthResult) {
//        if (result.additionalUserInfo?.isNewUser == true) {
//            var randomUsername = generateRandomUsername()
//            val existName = firestore.collection("users")
//                .document(generateRandomUsername())
//                .get()
//                .await()
//            while (existName.exists()) {
//                randomUsername = generateRandomUsername()
//            }
//            val profileUpdates = UserProfileChangeRequest.Builder()
//                .setDisplayName(randomUsername)
//                .build()
//
//            result.user?.updateProfile(profileUpdates)?.await()
//            firestore.collection(USER_NAME_COLLECTION)
//                .document(randomUsername)
//                .set(mapOf(USER_NAME_ID to result.user?.uid))
//                .await()
//
//        }
//    }
}