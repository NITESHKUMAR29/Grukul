package com.example.feature_auth.presentation

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_common.resut.ResultState
import com.example.core_model.models.User
import com.example.core_model.models.UserRole
import com.example.feature_auth.domain.useCase.SaveUserUseCase
import com.example.feature_auth.domain.useCase.SendOtpUseCase
import com.example.feature_auth.domain.useCase.VerifyOtpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val sendOtpUseCase: SendOtpUseCase,
    private val verifyOtpUseCase: VerifyOtpUseCase,
    private val firebaseAuthManager: com.example.core_firebase.auth.FirebaseAuthManager,
    private val saveUserUseCase: SaveUserUseCase
) : ViewModel() {

    private var verificationId: String? = null
    var phoneNumber: String? = null
    private var currentFirebaseUser: User? = null

    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state = _state.asStateFlow()

    // ✅ SEND OTP
    fun sendOtp(activity: Activity, phone: String) {
        phoneNumber = phone

        // Attach activity ONLY for Firebase
        firebaseAuthManager.attachActivity(activity)

        viewModelScope.launch {
            sendOtpUseCase(phone).collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _state.value = AuthState.Loading
                    }

                    is ResultState.Success -> {
                        verificationId = result.data
                        _state.value = AuthState.CodeSent
                    }

                    is ResultState.Error -> {
                        _state.value = AuthState.Error(result.message)
                    }
                }
            }
        }
    }

    // ✅ RESEND OTP
    fun resendOtp() {
        val phone = phoneNumber ?: return

        viewModelScope.launch {
            sendOtpUseCase.resend(phone).collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _state.value = AuthState.Loading
                    }

                    is ResultState.Success -> {
                        verificationId = result.data
                        _state.value = AuthState.CodeSent
                    }

                    is ResultState.Error -> {
                        _state.value = AuthState.Error(result.message)
                    }
                }
            }
        }
    }

    // ✅ VERIFY OTP
    fun verifyOtp(otp: String) {
        val id = verificationId ?: run {
            _state.value = AuthState.Error("OTP expired. Please retry.")
            return
        }

        viewModelScope.launch {
            verifyOtpUseCase(id, otp).collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _state.value = AuthState.Loading
                    }

                    is ResultState.Success -> {
                        _state.value = AuthState.Success(result.data)
                        currentFirebaseUser = result.data
                    }

                    is ResultState.Error -> {
                        _state.value = AuthState.Error(result.message)
                    }
                }
            }
        }
    }

    fun saveUser(name: String, role: UserRole) {
        Log.d("AuthViewModelCheck", "saveUser called with name: $phoneNumber, role: $currentFirebaseUser")
        val phone = phoneNumber ?: return
        val firebaseUser = currentFirebaseUser ?: return

        val user = firebaseUser.copy(
            name = name,
            role = role,
            phone = phone
        )

        viewModelScope.launch {
            saveUserUseCase(user).collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _state.value = AuthState.Loading
                    }
                    is ResultState.Success -> {
                        _state.value = AuthState.Success(user)
                    }
                    is ResultState.Error -> {
                        _state.value = AuthState.Error(result.message)
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        firebaseAuthManager.detachActivity()
    }

    fun resetAuthFlow() {
        verificationId = null
        phoneNumber = null
        _state.value = AuthState.Idle
    }

    fun resetStateOnly() {
        _state.value = AuthState.Idle
    }
}
