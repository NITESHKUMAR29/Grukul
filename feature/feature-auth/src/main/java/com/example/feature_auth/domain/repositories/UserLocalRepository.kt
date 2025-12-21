package com.example.feature_auth.domain.repositories

import kotlinx.coroutines.flow.Flow

interface UserLocalRepository {
    suspend fun saveUserId(userId: String)
    fun observeUserId(): Flow<String?>
    suspend fun clear()
}