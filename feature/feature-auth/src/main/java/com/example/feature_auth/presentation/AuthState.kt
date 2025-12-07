package com.example.feature_auth.presentation

import com.example.core_model.models.User

sealed class AuthState {

    object Idle : AuthState()

    object Loading : AuthState()

    object CodeSent : AuthState()

    data class Success(val user: User) : AuthState()

    data class Error(val message: String) : AuthState()
}