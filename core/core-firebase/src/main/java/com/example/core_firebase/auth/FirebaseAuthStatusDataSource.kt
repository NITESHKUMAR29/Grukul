package com.example.core_firebase.auth

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class FirebaseAuthStatusDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    fun isLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }
}