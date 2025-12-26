package com.example.feature_auth.domain.useCase


import com.example.core_common.resut.ResultState
import com.example.feature_auth.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SendOtpUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(phone: String): Flow<ResultState<String>> {
        return repository.sendOtp(phone)
    }

    fun resend(phone: String): Flow<ResultState<String>> {
        return repository.resendOtp(phone)
    }
}
