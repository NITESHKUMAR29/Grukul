package com.example.feature_class.domain.useCase

import com.example.feature_class.domain.models.ClassModel
import com.example.feature_class.domain.repository.ClassRepository
import javax.inject.Inject

class GetClassByIdUseCase @Inject constructor(
    private val repository: ClassRepository
) {
    suspend operator fun invoke(classId: String): ClassModel {
        return repository.getClassById(classId)
    }
}
