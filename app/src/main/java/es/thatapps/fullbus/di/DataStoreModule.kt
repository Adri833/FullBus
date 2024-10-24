package es.thatapps.fullbus.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

// Extensión de Context para crear una instancia de DataStore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Module
@InstallIn(SingletonComponent::class) // Proporciona la instancia a nivel de aplicación
object DataStoreModule {

    // Proporciona una instancia de DataStore
    @Provides
    fun provideDataStore(context: Context): DataStore<Preferences> {
        return context.dataStore
    }
}
