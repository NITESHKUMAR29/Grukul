package com.example.feature_class.data.mapper

import com.example.core_database.entity.ClassEntity
import com.example.feature_class.domain.models.ClassModel
import javax.inject.Inject

class ClassMapper @Inject constructor() {

    fun entityToDomain(entity: ClassEntity): ClassModel =
        ClassModel(
            id = entity.id,
            className = entity.className,
            teacherName = entity.teacherName,
            isActive = entity.isActive,
            gender = entity.gender,
            address = entity.address,
            createdBy = entity.createdBy,
            days = entity.days,
            startDate = entity.startDate,
            endDate = entity.endDate,
            createdAt = entity.updatedAt
        )

    fun domainToEntity(model: ClassModel): ClassEntity =
        ClassEntity(
            id = model.id,
            className = model.className,
            teacherName = model.teacherName,
            isActive = model.isActive,
            gender = model.gender,
            address = model.address,
            createdBy = model.createdBy,
            days = model.days,
            startDate = model.startDate,
            endDate = model.endDate,
            updatedAt = System.currentTimeMillis()
        )
}