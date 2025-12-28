package com.example.feature_auth.domain.useCase


import com.example.core_common.resut.UiState
import com.example.feature_auth.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SendOtpUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(phone: String): Flow<UiState<String>> {
        return repository.sendOtp(phone)
    }

    fun resend(phone: String): Flow<UiState<String>> {
        return repository.resendOtp(phone)
    }
}
