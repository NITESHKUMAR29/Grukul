package com.example.feature_class.data

import com.example.core_database.dao.ClassDao
import com.example.core_firebase.firestore.classes.FirebaseClassDataSource
import com.example.feature_class.domain.models.ClassModel
import com.example.feature_class.data.mapper.ClassMapper
import com.example.feature_class.data.mapper.ClassRemoteMapper
import com.example.feature_class.domain.repository.ClassRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ClassRepositoryImpl @Inject constructor(
    private val classDao: ClassDao,
    private val firebaseDataSource: FirebaseClassDataSource,
    private val mapper: ClassMapper,
    private val remoteMapper: ClassRemoteMapper
) : ClassRepository {


    override suspend fun createClass(classModel: ClassModel) {
        val dto = remoteMapper.entityToDto(
            mapper.domainToEntity(classModel)
        )
        firebaseDataSource.createClass(dto)

    }


    override suspend fun softDeleteClass(id: String) {
        firebaseDataSource.softDeleteClass(id)

    }


    override fun observeClasses(createdBy: String): Flow<List<ClassModel>> {
        return classDao.observeClassesByUser(createdBy)
            .map { it.map(mapper::entityToDomain) }
    }

    override suspend fun syncClasses(createdBy: String) {
        val remoteDtos = firebaseDataSource.fetchClasses(createdBy)
        val activeDtos = remoteDtos.filter { !it.isDeleted }
        val entities = activeDtos.map(remoteMapper::dtoToEntity)

        classDao.replaceAllForUser(createdBy, entities)
    }

    override suspend fun getClassById(classId: String): ClassModel {
        val entity = classDao.getClassById(classId)

        return entity?.let {
            mapper.entityToDomain(it)
        } ?: throw IllegalStateException("Class not found with id: $classId")
    }

    override suspend fun updateClass(classModel: ClassModel) {
        val dto = remoteMapper.entityToDto(
            mapper.domainToEntity(classModel)
        )
        firebaseDataSource.updateClass(dto)

        classDao.upsert(mapper.domainToEntity(classModel))
    }

}
