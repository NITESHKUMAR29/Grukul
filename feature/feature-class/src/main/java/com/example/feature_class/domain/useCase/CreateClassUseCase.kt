package com.example.feature_class.domain.useCase

import com.example.feature_class.domain.models.ClassModel
import com.example.feature_class.domain.repository.ClassRepository
import javax.inject.Inject


class CreateClassUseCase @Inject constructor(
    private val repository: ClassRepository
) {
    suspend operator fun invoke(classModel: ClassModel) {
        repository.createClass(classModel)
    }
}