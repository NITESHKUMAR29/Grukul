package com.example.core_firebase.auth

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class FirebaseAuthStatusDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    fun isLoggedIn(): Boolean {
        Log.d("FirebaseAuthStatusDataSource", "isLoggedIn: ${firebaseAuth.currentUser != null}")
        return firebaseAuth.currentUser != null
    }
}