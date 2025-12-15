package com.example.feature_auth.domain.repositories

import com.example.core_common.resut.ResultState
import com.example.core_model.models.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun sendOtp(phone: String): Flow<ResultState<String>>

    fun verifyOtp(
        verificationId: String,
        otp: String
    ): Flow<ResultState<User>>

    fun resendOtp(phone: String): Flow<ResultState<String>>

    fun saveUser(user: User): Flow<ResultState<Unit>>
}