package com.example.core_model.models

data class User(
    val id: String,
    val phone: String,
    val email: String = "",
    val name: String = "",
    val profilePic: String = "",
    val subject: String="",
    val role: UserRole = UserRole.UNKNOWN,
    val isNewUser: Boolean = false
)

enum class UserRole {
    STUDENT,
    TEACHER,
    ADMIN,
    UNKNOWN
}