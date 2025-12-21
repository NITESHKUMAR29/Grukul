package com.example.feature_auth.domain.useCase

import com.example.feature_auth.domain.repositories.UserLocalRepository
import javax.inject.Inject

class SaveUserIdUseCase @Inject constructor( private val userLocalRepository: UserLocalRepository) {
    suspend operator fun invoke(userId: String) {
        userLocalRepository.saveUserId(userId)
    }
}