package com.example.feature_auth.domain.useCase

import com.example.feature_auth.domain.repositories.AuthRepository
import javax.inject.Inject

class CheckAuthStatusUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): Boolean {
        return repository.isUserLoggedIn()
    }
}