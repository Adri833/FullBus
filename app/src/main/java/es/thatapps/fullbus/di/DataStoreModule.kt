package es.thatapps.fullbus.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import es.thatapps.fullbus.data.preferences.BusPreferences
import javax.inject.Singleton

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

    @Provides
    @Singleton
    fun provideBusPreferences(@ApplicationContext context: Context): BusPreferences {
        return BusPreferences(context)
    }
}
