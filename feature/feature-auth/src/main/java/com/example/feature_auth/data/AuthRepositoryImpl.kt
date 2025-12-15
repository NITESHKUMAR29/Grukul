package com.example.feature_auth.data

import com.example.core_common.resut.ResultState
import com.example.core_firebase.auth.FIreBaseUserDataSource
import com.example.core_firebase.auth.FirebaseAuthManager
import com.example.core_model.models.User
import com.example.feature_auth.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuthManager: FirebaseAuthManager,
    private val firebaseUserDataSource: FIreBaseUserDataSource
) : AuthRepository {

    override fun sendOtp(phone: String): Flow<ResultState<String>> {
        return firebaseAuthManager.sendOtp(phone)
    }

    override fun resendOtp(phone: String): Flow<ResultState<String>> {
        return firebaseAuthManager.resendOtp(phone)
    }

    override fun saveUser(user: User): Flow<ResultState<Unit>> {
        return firebaseUserDataSource.saveUser(user)
    }

    override fun verifyOtp(
        verificationId: String,
        otp: String
    ): Flow<ResultState<User>> {
        return firebaseAuthManager.verifyOtp(verificationId, otp)
    }
}


