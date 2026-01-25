package com.example.core_database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.core_model.models.ClassSchedule

@Entity(tableName = "classes")
data class ClassEntity(
    @PrimaryKey val id: String,
    val className: String,
    val teacherName: String,
    val isActive: Boolean,
    val gender: String,
    val address: String,
    val description: String,
    val createdBy: String,
    val schedules: List<ClassSchedule>,
    val startDate: Long,
    val endDate: Long,
    val updatedAt: Long,
    val isDeleted: Boolean = false
)

