package com.example.core_firebase.firestore.classes

import com.example.core_model.models.ClassSchedule

data class ClassRemoteDto(
    val id: String = "",
    val className: String = "",
    val teacherName: String = "",
    val active: Boolean = false,
    val gender: String = "",
    val address: String = "",
    val createdBy: String = "",
    val description: String = "",
    val schedules: List<ClassSchedule> = emptyList(),
    val startDate: Long = 0L,
    val endDate: Long = 0L,
    val updatedAt: Long = 0L,
    val isDeleted: Boolean = false
)