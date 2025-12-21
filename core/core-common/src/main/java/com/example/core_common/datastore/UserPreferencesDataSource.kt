package com.example.core_common.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesDataSource @Inject constructor(
    @field:ApplicationContext private val context: Context
) {

    suspend fun saveUserId(userId: String) {
        context.userDataStore.edit { prefs ->
            prefs[UserPrefKeys.USER_ID] = userId
        }
    }

    fun observeUserId(): Flow<String?> =
        context.userDataStore.data.map { prefs ->
            prefs[UserPrefKeys.USER_ID]
        }

    suspend fun clear() {
        context.userDataStore.edit { it.clear() }
    }
}