package com.example.feature_auth.domain.repositories

import com.example.core_common.resut.UiState
import com.example.core_model.models.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun sendOtp(phone: String): Flow<UiState<String>>

    fun verifyOtp(
        verificationId: String,
        otp: String
    ): Flow<UiState<User>>

    fun resendOtp(phone: String): Flow<UiState<String>>

    fun saveUser(user: User): Flow<UiState<Unit>>

    fun isUserLoggedIn(): Boolean
}