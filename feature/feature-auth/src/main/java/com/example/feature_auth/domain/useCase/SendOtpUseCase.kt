package com.example.feature_auth.domain.useCase

import android.app.Activity
import com.example.feature_auth.domain.AuthRepository
import javax.inject.Inject

class SendOtpUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(
        phone: String,
        activity: Activity,
        onCodeSent: (String) -> Unit,
        onError: (String) -> Unit
    ) = repository.sendOtp(phone, activity, onCodeSent, onError)

    fun resendOtp(
        phone: String,
        activity: Activity,
        onCodeSent: (String) -> Unit,
        onError: (String) -> Unit
    ) = repository.resendOtp(phone, activity, onCodeSent, onError)
}
