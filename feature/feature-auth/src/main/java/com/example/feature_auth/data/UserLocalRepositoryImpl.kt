package com.example.feature_auth.data

import com.example.core_common.datastore.UserPreferencesDataSource
import com.example.feature_auth.domain.repositories.UserLocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserLocalRepositoryImpl @Inject constructor(private val dataSource: UserPreferencesDataSource):
    UserLocalRepository {
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