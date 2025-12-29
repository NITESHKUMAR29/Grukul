package com.example.feature_class.domain.repository

import com.example.feature_class.domain.models.ClassModel
import kotlinx.coroutines.flow.Flow

interface ClassRepository {

    suspend fun createClass(classModel: ClassModel)

    suspend fun softDeleteClass(id: String)

    fun observeClasses(): Flow<List<ClassModel>>

    suspend fun syncClasses()
}