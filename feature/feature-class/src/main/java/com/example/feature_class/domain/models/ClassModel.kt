package com.example.feature_class.domain.models

data class ClassModel(
    val id: String = "",
    val className: String = "",
    val teacherName: String = "",
    val isActive: Boolean = true,
    val gender: String = "",
    val address: String = "",
    val createdBy: String = "",
    val days: List<Int>,
    val startDate: Long=0,
    val description: String = "",
    val endDate: Long=0,
    val createdAt: Long = System.currentTimeMillis()
)