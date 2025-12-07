package com.example.feature_auth.data

import android.app.Activity
import com.example.core_firebase.auth.FirebaseAuthManager
import com.example.core_model.models.User
import com.example.feature_auth.domain.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuthManager: FirebaseAuthManager
) : AuthRepository {

    override fun sendOtp(
        phone: String,
        activity: Activity,
        onCodeSent: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        firebaseAuthManager.sendOtp(
            phone = phone,
            activity = activity,
            onCodeSent = onCodeSent,
            onError = onError
        )
    }

    override fun verifyOtp(
        verificationId: String,
        otp: String,
        onSuccess: (User) -> Unit,
        onError: (String) -> Unit
    ) {
        firebaseAuthManager.verifyOtp(
            verificationId = verificationId,
            otp = otp,
            onSuccess = onSuccess,
            onError = onError
        )
    }

    override fun resendOtp(
        phone: String,
        activity: Activity,
        onCodeSent: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        firebaseAuthManager.resendOtp(
            phone,
            activity,
            onCodeSent,
            onError
        )
    }
}