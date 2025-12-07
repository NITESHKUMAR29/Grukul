package com.example.feature_auth.domain.useCase

import com.example.core_model.models.User
import com.example.feature_auth.domain.AuthRepository
import javax.inject.Inject

class VerifyOtpUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(
        verificationId: String,
        otp: String,
        onSuccess: (User) -> Unit,
        onError: (String) -> Unit
    ) = repository.verifyOtp(verificationId, otp, onSuccess, onError)
}