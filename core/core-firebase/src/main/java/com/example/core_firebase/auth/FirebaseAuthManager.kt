package com.example.core_firebase.auth

import android.app.Activity
import com.example.core_model.models.User
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FirebaseAuthManager @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val mapper: FirebaseUserMapper
) {

    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    fun clearSession() {
        firebaseAuth.signOut()
        resendToken = null
    }


    fun sendOtp(
        phone: String,
        activity: Activity,
        onCodeSent: (String) -> Unit,
        onError: (String) -> Unit
    ) {

        clearSession() // ✅ strong reset

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phone)
            .setActivity(activity)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setCallbacks(callbacks(onCodeSent, onError))
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }


    fun resendOtp(
        phone: String,
        activity: Activity,
        onCodeSent: (String) -> Unit,
        onError: (String) -> Unit
    ) {

        val token = resendToken
            ?: run {
                onError("Cannot resend yet. Try again later.")
                return
            }

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phone)
            .setActivity(activity)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setCallbacks(callbacks(onCodeSent, onError))
            .setForceResendingToken(token) // ✅ reuse token
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }


    fun verifyOtp(
        verificationId: String,
        otp: String,
        onSuccess: (User) -> Unit,
        onError: (String) -> Unit
    ) {

        val credential = PhoneAuthProvider.getCredential(verificationId, otp)

        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                val user = it.user
                if (user != null) {
                    onSuccess(mapper.map(user))
                } else {
                    onError("User mapping failed")
                }
            }
            .addOnFailureListener {
                onError(it.localizedMessage ?: "Invalid OTP")
            }
    }


    private fun callbacks(
        onCodeSent: (String) -> Unit,
        onError: (String) -> Unit
    ) = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // ✅ Ignore auto sign-in (manual flow better UX)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            onError(e.localizedMessage ?: "OTP verification failed")
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            resendToken = token
            onCodeSent(verificationId)
        }
    }
}
