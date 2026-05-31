package com.alavpa.colors.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.alavpa.colors.domain.repository.UserPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferencesRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : UserPreferencesRepository {

    private object PreferencesKeys {
        val IS_ADS_REMOVED = booleanPreferencesKey("is_ads_removed")
        val REMAINING_HINTS = intPreferencesKey("remaining_hints")
        val CURRENT_LEVEL = intPreferencesKey("current_level")
    }

    override val isAdsRemoved: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.IS_ADS_REMOVED] ?: false
    }

    override val remainingHints: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.REMAINING_HINTS] ?: 3 // Start with 3 hints
    }

    override val currentLevel: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.CURRENT_LEVEL] ?: 1 // Start at level 1
    }

    override suspend fun setAdsRemoved(removed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_ADS_REMOVED] = removed
        }
    }

    override suspend fun setRemainingHints(hints: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.REMAINING_HINTS] = hints
        }
    }

    override suspend fun setCurrentLevel(level: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.CURRENT_LEVEL] = level
        }
    }
}
