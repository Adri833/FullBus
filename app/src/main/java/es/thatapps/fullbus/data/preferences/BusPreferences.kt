package es.thatapps.fullbus.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BusPreferences(context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "bus_preferences")
    private val dataStore = context.dataStore

    // Genera una clave para cada línea de autobús
    private fun getBusKey(busLineId: String) = booleanPreferencesKey("is_bus_full_$busLineId")

    // Flow para obtener el estado actual de una línea de autobús específica
    fun isBusFull(busLineId: String): Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[getBusKey(busLineId)] ?: false
        }

    // Función para cambiar el estado de bus lleno para una línea específica
    suspend fun setBusFull(busLineId: String, isFull: Boolean) {
        dataStore.edit { preferences ->
            preferences[getBusKey(busLineId)] = isFull
        }
    }
}
