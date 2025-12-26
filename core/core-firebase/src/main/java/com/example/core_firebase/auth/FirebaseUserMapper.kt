package com.example.core_firebase.auth

import android.annotation.SuppressLint
import com.google.firebase.auth.FirebaseUser
import com.example.core_model.models.User

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
