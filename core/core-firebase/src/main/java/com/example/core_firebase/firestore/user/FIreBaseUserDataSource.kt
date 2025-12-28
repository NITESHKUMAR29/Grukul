package com.example.core_firebase.firestore.user

import com.example.core_common.resut.UiState
import com.example.core_model.models.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FIreBaseUserDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    fun saveUser(user: User): Flow<UiState<Unit>> = callbackFlow {
        trySend(UiState.Loading)

        firestore.collection("users")
            .document(user.id)
            .set(user)
            .addOnSuccessListener {
                trySend(UiState.Success(Unit))
                close()
            }
            .addOnFailureListener {
                trySend(UiState.Error(it.localizedMessage ?: "Failed to save user"))
                close()
            }

        awaitClose { }
    }
}