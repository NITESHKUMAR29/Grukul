package com.example.feature_class.domain.models

data class ClassModel(
    val id: String = "",
    val className: String = "",
    val teacherName: String = "",
    val isActive: Boolean = true,
    val gender: String = "",
    val address: String = "",
    val createdBy: String = "",
    val schedule: String,
    val createdAt: Long = System.currentTimeMillis()
)