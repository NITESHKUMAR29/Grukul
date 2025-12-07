package com.example.feature_auth.presentation

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.feature_auth.domain.useCase.SendOtpUseCase
import com.example.feature_auth.domain.useCase.VerifyOtpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val sendOtpUseCase: SendOtpUseCase,
    private val verifyOtpUseCase: VerifyOtpUseCase
) : ViewModel() {

    var verificationId: String? = null
        private set

    var phoneNumber: String? = null
    private var activityRef: Activity? = null

    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state = _state.asStateFlow()


    fun sendOtp(activity: Activity, phone: String) {

        activityRef = activity
        phoneNumber = phone

        _state.value = AuthState.Loading
        Log.d("AuthVM", "sendOtp: $phone")

        sendOtpUseCase(
            phone,
            activity,
            onCodeSent = { id ->
                Log.d("AuthVM", "VerificationID: $id")
                verificationId = id
                _state.value = AuthState.CodeSent
            },
            onError = {
                _state.value = AuthState.Error(it)
            }
        )
    }


    fun resendOtp() {

        val phone = phoneNumber ?: return
        val activity = activityRef ?: return

        _state.value = AuthState.Loading

        sendOtpUseCase.resendOtp(
            phone,
            activity,
            onCodeSent = {
                verificationId = it
                _state.value = AuthState.CodeSent
            },
            onError = {
                _state.value = AuthState.Error(it)
            }
        )
    }


    fun verifyOtp(otp: String) {

        val id = verificationId ?: run {
            _state.value = AuthState.Error("OTP expired. Please retry.")
            return
        }

        _state.value = AuthState.Loading

        verifyOtpUseCase(
            id,
            otp,
            onSuccess = {
                _state.value = AuthState.Success(it)
            },
            onError = {
                _state.value = AuthState.Error(it)
            }
        )
    }


    fun resetAuthFlow() {
        verificationId = null
        phoneNumber = null
        activityRef = null
        _state.value = AuthState.Idle
    }


    fun resetStateOnly() {
        _state.value = AuthState.Idle
    }
}

