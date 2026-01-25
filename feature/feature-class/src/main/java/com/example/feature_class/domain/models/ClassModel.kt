package com.example.feature_class.domain.models

import com.example.core_model.models.ClassSchedule

data class ClassModel(
    val id: String = "",
    val className: String = "",
    val teacherName: String = "",
    val isActive: Boolean = true,
    val gender: String = "",
    val address: String = "",
    val createdBy: String = "",
    val schedules: List<ClassSchedule>,
    val startDate: Long=0,
    val description: String = "",
    val endDate: Long=0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)