package com.example.core_ui.ui.toast

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun ToastHost(
    toastState: ToastState,
    modifier: Modifier = Modifier
) {
    val toast = toastState.currentToast

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 110.dp)
            .padding(horizontal = 16.dp),

        contentAlignment = Alignment.BottomCenter
    ) {
        if (toast != null) {
            CustomToast(
                message = toast.message,
                type = toast.type,
                onDismiss = { toastState.clear() }
            )
        }
    }
}