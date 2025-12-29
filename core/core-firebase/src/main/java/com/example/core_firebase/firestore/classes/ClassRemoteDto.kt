package com.example.core_firebase.firestore.classes

data class ClassRemoteDto(
    val id: String = "",
    val className: String = "",
    val teacherName: String = "",
    val active: Boolean = false,
    val gender: String = "",
    val address: String = "",
    val createdBy: String = "",
    val updatedAt: Long = 0L,
    val schedule: String = "",
    val isDeleted: Boolean = false
)