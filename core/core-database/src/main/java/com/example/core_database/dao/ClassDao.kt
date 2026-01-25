package com.example.core_database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.core_database.entity.ClassEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClassDao {

    @Query(
        """
        SELECT * FROM classes
        WHERE createdBy = :createdBy
          AND isDeleted = 0
        ORDER BY updatedAt DESC
        """
    )
    fun observeClassesByUser(createdBy: String): Flow<List<ClassEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: ClassEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<ClassEntity>)

    @Query(
        """
        UPDATE classes
        SET isDeleted = 1,
            updatedAt = :timestamp
        WHERE id = :classId
        """
    )
    suspend fun softDelete(
        classId: String,
        timestamp: Long
    )

    @Query(
        """
        DELETE FROM classes
        WHERE createdBy = :createdBy
        """
    )
    suspend fun deleteAllForUser(createdBy: String)

    @Query(
        """
        DELETE FROM classes
        WHERE createdBy = :createdBy
        """
    )
    suspend fun clearUserData(createdBy: String)
    @Transaction
    suspend fun replaceAllForUser(
        createdBy: String,
        entities: List<ClassEntity>
    ) {
        deleteAllForUser(createdBy)
        insertAll(entities)
    }


    @Query("SELECT * FROM classes WHERE id = :classId LIMIT 1")
    suspend fun getClassById(classId: String): ClassEntity?
}