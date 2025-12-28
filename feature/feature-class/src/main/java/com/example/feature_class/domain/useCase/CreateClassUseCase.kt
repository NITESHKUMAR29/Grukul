package com.example.feature_class.domain.useCase

import com.example.core_common.resut.UiState
import com.example.feature_auth.domain.models.ClassModel
import com.example.feature_class.domain.repository.ClassRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class CreateClassUseCase @Inject constructor(
    private val repository: ClassRepository
) {
    suspend operator fun invoke(classModel: ClassModel) {
        repository.createClass(classModel)
    }
}