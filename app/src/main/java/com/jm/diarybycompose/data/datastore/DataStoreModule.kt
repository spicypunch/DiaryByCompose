package com.jm.diarybycompose.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okio.IOException

class DataStoreModule(private val context: Context) {

    companion object {
        private val Context.dataStore by preferencesDataStore(name = "dataStore")
        private val notificationState = booleanPreferencesKey("isEnabled")
    }

    val getNotificationState: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preference ->
            preference[notificationState] ?: false
        }


    suspend fun saveNotificationState(state: Boolean) {
        context.dataStore.edit { preference ->
            preference[notificationState] = state
        }
    }
}