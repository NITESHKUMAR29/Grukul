package com.example.core_firebase.firestore.classes


import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseClassDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun fetchClasses(createdBy: String): List<ClassRemoteDto> {
        return firestore.collection("classes")
            .whereEqualTo("createdBy",createdBy)
            .get()
            .await()
            .documents
            .mapNotNull { doc ->
                doc.toObject(ClassRemoteDto::class.java)
                    ?.copy(id = doc.id)
            }
    }



    suspend fun createClass(dto: ClassRemoteDto) {
        firestore.collection("classes")
            .document(dto.id)
            .set(dto)
            .await()
    }

    suspend fun updateClass(dto: ClassRemoteDto) {
        firestore.collection("classes")
            .document(dto.id)
            .set(dto, SetOptions.merge())
            .await()
    }
    suspend fun softDeleteClass(id: String) {
        firestore.collection("classes")
            .document(id)
            .update(
                mapOf(
                    "isDeleted" to true,
                    "updatedAt" to System.currentTimeMillis()
                )
            )
            .await()
    }
}