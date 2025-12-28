package com.example.feature_auth.data

import com.example.core_common.resut.UiState
import com.example.core_firebase.firestore.user.FIreBaseUserDataSource
import com.example.core_firebase.auth.FirebaseAuthManager
import com.example.core_firebase.auth.FirebaseAuthStatusDataSource
import com.example.core_model.models.User
import com.example.feature_auth.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuthManager: FirebaseAuthManager,
    private val firebaseUserDataSource: FIreBaseUserDataSource,
    private val authStatusDataSource: FirebaseAuthStatusDataSource
) : AuthRepository {

    override fun sendOtp(phone: String): Flow<UiState<String>> {
        return firebaseAuthManager.sendOtp(phone)
    }

    override fun resendOtp(phone: String): Flow<UiState<String>> {
        return firebaseAuthManager.resendOtp(phone)
    }

    override fun saveUser(user: User): Flow<UiState<Unit>> {
        return firebaseUserDataSource.saveUser(user)
    }

    override fun isUserLoggedIn(): Boolean {
        return authStatusDataSource.isLoggedIn()
    }

    override fun verifyOtp(
        verificationId: String,
        otp: String
    ): Flow<UiState<User>> {
        return firebaseAuthManager.verifyOtp(verificationId, otp)
    }
}


