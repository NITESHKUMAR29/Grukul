package com.example.core_firebase.auth

import com.example.core_common.resut.ResultState
import com.example.core_model.models.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FIreBaseUserDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    fun saveUser(user: User): Flow<ResultState<Unit>> = callbackFlow {
        trySend(ResultState.Loading)

        firestore.collection("users")
            .document(user.id)
            .set(user)
            .addOnSuccessListener {
                trySend(ResultState.Success(Unit))
                close()
            }
            .addOnFailureListener {
                trySend(ResultState.Error(it.localizedMessage ?: "Failed to save user"))
                close()
            }

        awaitClose { }
    }
}