package com.example.core_firebase.auth

import android.app.Activity
import com.example.core_common.resut.UiState
import com.example.core_firebase.firestore.user.FirebaseUserMapper
import com.example.core_model.models.User
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FirebaseAuthManager @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val mapper: FirebaseUserMapper
) {

    private var activity: Activity? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    fun attachActivity(activity: Activity) {
        this.activity = activity
    }

    fun detachActivity() {
        this.activity = null
    }

    fun sendOtp(phone: String): Flow<UiState<String>> = callbackFlow {
        val currentActivity = activity
        if (currentActivity == null) {
            trySend(UiState.Error("Activity not attached"))
            close()
            return@callbackFlow
        }

        trySend(UiState.Loading)

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationFailed(e: FirebaseException) {
                trySend(UiState.Error(e.localizedMessage ?: "OTP failed"))
                close()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                resendToken = token
                trySend(UiState.Success(verificationId))
                close()
            }

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {}
        }

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phone)
            .setActivity(currentActivity)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)

        awaitClose { }
    }

    fun resendOtp(phone: String): Flow<UiState<String>> = callbackFlow {
        val currentActivity = activity
        if (currentActivity == null) {
            trySend(UiState.Error("Activity not attached"))
            close()
            return@callbackFlow
        }

        val token = resendToken
        if (token == null) {
            trySend(UiState.Error("Cannot resend yet"))
            close()
            return@callbackFlow
        }

        trySend(UiState.Loading)

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // ignored
            }

            override fun onVerificationFailed(e: FirebaseException) {
                trySend(UiState.Error(e.localizedMessage ?: "Resend failed"))
                close()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                resendToken = token
                trySend(UiState.Success(verificationId))
                close()
            }
        }

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phone)
            .setActivity(currentActivity)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setForceResendingToken(token)
            .setCallbacks(callbacks)   // ✅ REQUIRED
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)

        awaitClose { }
    }

    fun verifyOtp(
        verificationId: String,
        otp: String
    ): Flow<UiState<User>> = callbackFlow {

        trySend(UiState.Loading)

        val credential = PhoneAuthProvider.getCredential(verificationId, otp)

        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener { result ->

                val firebaseUser = result.user
                val isNew = result.additionalUserInfo?.isNewUser == true

                if (firebaseUser != null) {
                    val domainUser = mapper.map(firebaseUser).copy(
                        isNewUser = isNew
                    )

                    trySend(UiState.Success(domainUser))
                } else {
                    trySend(UiState.Error("User mapping failed"))
                }
                close()
            }
            .addOnFailureListener {
                trySend(UiState.Error(it.localizedMessage ?: "Invalid OTP"))
                close()
            }

        awaitClose { }
    }
}