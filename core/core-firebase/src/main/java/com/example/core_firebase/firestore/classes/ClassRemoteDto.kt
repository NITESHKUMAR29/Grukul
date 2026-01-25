package com.example.core_firebase.firestore.classes

data class ClassRemoteDto(
    val id: String = "",
    val className: String = "",
    val teacherName: String = "",
    val active: Boolean = false,
    val gender: String = "",
    val address: String = "",
    val createdBy: String = "",
    val description: String = "",
    val days: List<Int> = emptyList(),
    val startDate: Long = 0L,
    val endDate: Long = 0L,
    val updatedAt: Long = 0L,
    val isDeleted: Boolean = false
)