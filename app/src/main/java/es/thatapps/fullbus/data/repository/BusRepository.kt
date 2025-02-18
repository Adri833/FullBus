package es.thatapps.fullbus.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import es.thatapps.fullbus.presentation.busDetails.domain.BusDetailDomain
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BusRepository @Inject constructor() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val busCollection = firestore.collection("autobuses")

    // Obtiene el id del autobus
    private fun getBusId(bus: BusDetailDomain): String =
        "${bus.line}_${bus.departureTime}_${bus.direction}"

    // Metodo que obtiene todos los autobuses
    suspend fun getActiveBuses(): List<BusDetailDomain> {
        val snapshot = busCollection.get().await()
        return snapshot.documents.mapNotNull { it.toObject<BusDetailDomain>() }
    }

    // Metodo que agrega un bus a la db
    suspend fun addBus(bus: BusDetailDomain) {
        val autobus = hashMapOf(
            "line" to bus.line,
            "isFull" to bus.isFull,
            "departureTime" to bus.departureTime,
            "arriveTime" to bus.arriveTime,
            "direction" to bus.direction,
            "id" to getBusId(bus),
            "day" to bus.day,
            "reportedByUsername" to bus.reportedByUsername,
            "reportedByPfp" to bus.reportedByPfp
        )

        busCollection.document(getBusId(bus)).set(autobus).await()
    }

    // Metodo para eliminar un bus de la db
    suspend fun deleteBus(bus: BusDetailDomain) {
        busCollection.document(getBusId(bus)).delete().await()
    }

    // Metodo que actualiza un bus en la db
    suspend fun updateBus(bus: BusDetailDomain) {
        val busDoc = busCollection
            .whereEqualTo("id", getBusId(bus))
            .get()
            .await()
            .documents
            .firstOrNull()

        // Actualiza el autobus con los datos nuevos
        busDoc?.let {
            busCollection.document(it.id).set(bus).await()
        }
    }

    // Metodo para recoger el nombre del bus reportado
    suspend fun getUserBus(bus: BusDetailDomain): String {
        return try {
            val document = firestore.collection("autobuses").document(getBusId(bus)).get().await()
            document.getString("reportedByUsername") ?: throw Exception("Username not found")
        } catch (e: Exception) {
            throw Exception("Error al obtener el username: ${e.message}", e)
        }
    }

    // Metodo para recoger el pfp del bus reportado
    suspend fun getPFPBus(bus: BusDetailDomain): String {
        return try {
            val document = firestore.collection("autobuses").document(getBusId(bus)).get().await()
            document.getString("reportedByPfp") ?: throw Exception("PFP not found")
        } catch (e: Exception) {
            throw Exception("Error al obtener el pfp: ${e.message}", e)
        }
    }
}