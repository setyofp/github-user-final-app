package com.setyofp.githubuserfinalapp.database

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(private val context: Context) {

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var mInstance: UserPreferences? = null

        fun getInstance(context: Context): UserPreferences =
            mInstance?: synchronized(this) {
                val newInstance = mInstance?: UserPreferences(context).also { mInstance = it }
                newInstance
            }
    }

    private val Context.userPreferenceDataStore: androidx.datastore.core.DataStore<Preferences> by preferencesDataStore(
        name = DataStore.DATA_STORE_NAME
    )

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        context.userPreferenceDataStore.edit {
            it[DataStore.DATA_STORE_PREF_THEME_KEY] = isDarkModeActive
        }
    }

    fun getThemeSetting(): Flow<Boolean> =
        context.userPreferenceDataStore.data.map {
            it[DataStore.DATA_STORE_PREF_THEME_KEY] ?: false
        }

    object DataStore {
        const val DATA_STORE_NAME = "USER_DATASTORE"
        val DATA_STORE_PREF_THEME_KEY = booleanPreferencesKey("THEME_PREF_KEY")
    }
}
