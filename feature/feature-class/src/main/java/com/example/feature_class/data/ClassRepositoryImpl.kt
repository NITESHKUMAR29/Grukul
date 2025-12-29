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


    override fun observeClasses(): Flow<List<ClassModel>> {
        return classDao.observeClasses()
            .map { entities ->
                entities.map(mapper::entityToDomain)
            }
    }


    override suspend fun syncClasses() {
        val remoteDtos = firebaseDataSource.fetchClasses()

        val activeDtos = remoteDtos.filter { dto ->
            !dto.isDeleted
        }

        val entities = activeDtos.map(remoteMapper::dtoToEntity)

        classDao.replaceAll(entities)
    }
}
