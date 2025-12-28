package com.example.feature_class.data

import android.util.Log
import com.example.core_database.dao.ClassDao
import com.example.core_firebase.firestore.classes.FirebaseClassDataSource
import com.example.feature_auth.domain.models.ClassModel
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
        val entity = mapper.domainToEntity(classModel)
        val dto = remoteMapper.entityToDto(entity)

        firebaseDataSource.createClass(dto)

        classDao.upsert(entity)
    }


    override fun observeClasses(): Flow<List<ClassModel>> {
        return classDao.observeClasses()
            .map { entities ->
                entities.map(mapper::entityToDomain)
            }
    }

    override suspend fun syncClasses() {
        val remoteDtos = firebaseDataSource.fetchClasses()
        remoteDtos.forEach {
            Log.d("SYNC_REMOTE", "isActive from firebase = ${it.active}")
        }
        val entities = remoteDtos.map(remoteMapper::dtoToEntity)
        classDao.upsertAll(entities)
    }
}
