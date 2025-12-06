package com.example.core_ui.ui.toast

import androidx.compose.runtime.mutableStateOf

class ToastState {

    private val _currentToast = mutableStateOf<ToastData?>(null)
    val currentToast: ToastData? get() = _currentToast.value

    fun show(message: String, type: ToastType) {
        _currentToast.value = ToastData(message, type)
    }

    fun clear() {
        _currentToast.value = null
    }
}

data class ToastData(
    val message: String,
    val type: ToastType
)