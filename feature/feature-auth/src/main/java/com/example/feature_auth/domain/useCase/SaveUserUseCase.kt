package com.example.feature_auth.domain.useCase

import com.example.core_common.resut.UiState
import com.example.core_model.models.User
import com.example.feature_auth.domain.repositories.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SaveUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(user: User): Flow<UiState<Unit>> {
        return repository.saveUser(user).flowOn(Dispatchers.IO)

    }
}