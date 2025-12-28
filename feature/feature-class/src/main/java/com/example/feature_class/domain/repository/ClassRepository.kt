package com.example.feature_class.domain.repository

import com.example.core_common.resut.UiState
import com.example.feature_auth.domain.models.ClassModel
import kotlinx.coroutines.flow.Flow

interface ClassRepository {

    suspend fun createClass(classModel: ClassModel)


    fun observeClasses(): Flow<List<ClassModel>>


    suspend fun syncClasses()
}