package com.example.feature_auth.domain

import android.app.Activity
import com.example.core_model.models.User

interface AuthRepository {

    fun sendOtp(
        phone: String,
        activity: Activity,
        onCodeSent: (String) -> Unit,
        onError: (String) -> Unit
    )

    fun verifyOtp(
        verificationId: String,
        otp: String,
        onSuccess: (User) -> Unit,
        onError: (String) -> Unit
    )
    fun resendOtp(
        phone: String,
        activity: Activity,
        onCodeSent: (String) -> Unit,
        onError: (String) -> Unit
    )
}