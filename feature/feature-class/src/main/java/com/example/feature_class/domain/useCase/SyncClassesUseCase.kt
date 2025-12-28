package com.example.feature_class.domain.useCase

import com.example.feature_class.domain.repository.ClassRepository
import javax.inject.Inject

class SyncClassesUseCase @Inject constructor(
    private val repository: ClassRepository
) {
    suspend operator fun invoke() {
        repository.syncClasses()
    }
}