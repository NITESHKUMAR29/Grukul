package com.example.feature_class.domain.useCase

import com.example.feature_auth.domain.models.ClassModel
import com.example.feature_class.domain.repository.ClassRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveClassesUseCase @Inject constructor(
    private val repository: ClassRepository
) {
    operator fun invoke(): Flow<List<ClassModel>> {
        return repository.observeClasses()
    }
}