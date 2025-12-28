package com.example.core_firebase.firestore.user

import android.annotation.SuppressLint
import com.example.core_model.models.User
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class FirebaseUserMapper @Inject constructor() {

    @SuppressLint("RestrictedApi")
    fun map(user: FirebaseUser): User {
        return User(
            id = user.uid,
            phone = user.phoneNumber ?: "",
            email = user.email ?: "",
            name = user.displayName ?: "",
            profilePic = user.photoUrl?.toString() ?: ""
        )
    }
}