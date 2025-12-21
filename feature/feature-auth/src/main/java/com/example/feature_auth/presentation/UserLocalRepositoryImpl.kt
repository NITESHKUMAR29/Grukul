package com.example.feature_auth.presentation

import com.example.core_common.datastore.UserPreferencesDataSource
import com.example.feature_auth.domain.repositories.UserLocalRepository
import kotlinx.coroutines.flow.Flow

class UserLocalRepositoryImpl(private val dataSource: UserPreferencesDataSource): UserLocalRepository {
    override suspend fun saveUserId(userId: String) {
        dataSource.saveUserId(userId )
    }

    override fun observeUserId(): Flow<String?> {
        return dataSource.observeUserId()
    }

    override suspend fun clear() {
        dataSource.clear()
    }
}