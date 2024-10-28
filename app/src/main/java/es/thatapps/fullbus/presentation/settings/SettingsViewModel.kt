package es.thatapps.fullbus.presentation.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences> // Inyección de DataStore
) : ViewModel() {

    private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
    private val NOTIFICATIONS_KEY = booleanPreferencesKey("notifications")

    private val _isDarkModeEnabled = MutableStateFlow(false)
    val isDarkModeEnabled: StateFlow<Boolean> = _isDarkModeEnabled

    private val _areNotificationsEnabled = MutableStateFlow(false)
    val areNotificationsEnabled: StateFlow<Boolean> = _areNotificationsEnabled

    // Carga las configuraciones al iniciar el viewmodel
    init {
        loadSettings()
    }

    // Carga las configuraciones desde el datastore
    private fun loadSettings() {
        viewModelScope.launch {
            dataStore.data.collect { preferences ->
                _isDarkModeEnabled.value = preferences[DARK_MODE_KEY] ?: false
                _areNotificationsEnabled.value = preferences[NOTIFICATIONS_KEY] ?: false
            }
        }
    }

    // Funcion para cambiar el estado de modo oscuro
    fun toggleDarkMode(isChecked: Boolean) {
        // Modifica el valor en el datastore
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[DARK_MODE_KEY] = isChecked
            }
            // Actualiza el dato interno
            _isDarkModeEnabled.value = isChecked
        }
    }

    // Funcion para cambiar el estado de notificaciones
    fun toggleNotifications(isChecked: Boolean) {
        // Modifica el valor en el datastore
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[NOTIFICATIONS_KEY] = isChecked
            }
            // Actualiza el dato interno
            _areNotificationsEnabled.value = isChecked
        }
    }
}