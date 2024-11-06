package es.thatapps.fullbus.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import es.thatapps.fullbus.presentation.busDetails.domain.BusDetailDomain
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BusRepository @Inject constructor() {
    private val firestore = FirebaseFirestore.getInstance()
    private val busCollection = firestore.collection("autobuses")

    // Metodo que obtiene todos los autobuses
    suspend fun getActiveBuses(): List<BusDetailDomain> {
        val snapshot = busCollection.get().await()
        return snapshot.documents.mapNotNull { it.toObject<BusDetailDomain>() }
    }

    // Metodo que agrega un bus a la db
    suspend fun addBus(bus: BusDetailDomain) {
        busCollection.add(bus).await()
    }

    // Metodo que actualiza un bus en la db
    suspend fun updateBus(bus: BusDetailDomain) {
        val busDoc = busCollection
            .whereEqualTo("line", bus.line) // 'line' es el identificador unico del autobus (filtro)
            .get()
            .await()
            .documents
            .firstOrNull()

        // Actualiza el autobus con los datos nuevos
        busDoc?.let {
            busCollection.document(it.id).set(bus).await()
        }
    }
}