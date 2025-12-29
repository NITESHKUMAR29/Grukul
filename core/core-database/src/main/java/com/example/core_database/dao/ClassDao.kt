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

    /* ---------------------------------------------------
     * READ
     * --------------------------------------------------- */

    /**
     * Observe classes for a specific user (offline-first).
     * Only non-deleted classes are emitted.
     */
    @Query(
        """
        SELECT * FROM classes
        WHERE createdBy = :createdBy
          AND isDeleted = 0
        ORDER BY updatedAt DESC
        """
    )
    fun observeClassesByUser(createdBy: String): Flow<List<ClassEntity>>

    /* ---------------------------------------------------
     * INSERT / UPDATE
     * --------------------------------------------------- */

    /**
     * Insert or update a single class.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: ClassEntity)

    /**
     * Insert or update multiple classes.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<ClassEntity>)

    /* ---------------------------------------------------
     * DELETE / SOFT DELETE
     * --------------------------------------------------- */

    /**
     * Soft delete a class locally.
     * Used only after remote delete success.
     */
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

    /**
     * Delete all classes for a specific user.
     * Used internally by replaceAllForUser().
     */
    @Query(
        """
        DELETE FROM classes
        WHERE createdBy = :createdBy
        """
    )
    suspend fun deleteAllForUser(createdBy: String)

    /**
     * Clear all data for a user (e.g. logout).
     */
    @Query(
        """
        DELETE FROM classes
        WHERE createdBy = :createdBy
        """
    )
    suspend fun clearUserData(createdBy: String)

    /* ---------------------------------------------------
     * SYNC HELPERS
     * --------------------------------------------------- */

    /**
     * Replace all cached classes for a user.
     * This keeps Room perfectly in sync with Firestore.
     */
    @Transaction
    suspend fun replaceAllForUser(
        createdBy: String,
        entities: List<ClassEntity>
    ) {
        deleteAllForUser(createdBy)
        insertAll(entities)
    }
}