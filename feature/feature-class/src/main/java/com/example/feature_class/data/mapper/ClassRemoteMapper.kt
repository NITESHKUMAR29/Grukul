package com.example.feature_class.data.mapper

import com.example.core_database.entity.ClassEntity
import com.example.core_firebase.firestore.classes.ClassRemoteDto
import javax.inject.Inject

class ClassRemoteMapper @Inject constructor() {

    fun dtoToEntity(dto: ClassRemoteDto): ClassEntity =
        ClassEntity(
            id = dto.id,
            className = dto.className,
            teacherName = dto.teacherName,
            isActive = dto.active,
            gender = dto.gender,
            address = dto.address,
            createdBy = dto.createdBy,
            schedule = dto.schedule,
            updatedAt = dto.updatedAt
        )

    fun entityToDto(entity: ClassEntity): ClassRemoteDto =
        ClassRemoteDto(
            id = entity.id,
            className = entity.className,
            teacherName = entity.teacherName,
            active = entity.isActive,
            gender = entity.gender,
            address = entity.address,
            createdBy = entity.createdBy,
            updatedAt = entity.updatedAt,
            schedule = entity.schedule,
            isDeleted = false
        )
}
