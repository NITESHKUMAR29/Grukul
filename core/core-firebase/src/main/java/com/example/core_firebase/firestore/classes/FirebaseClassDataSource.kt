package com.example.core_firebase.firestore.classes


import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseClassDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun fetchClasses(): List<ClassRemoteDto> {
        return firestore.collection("classes")
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
}