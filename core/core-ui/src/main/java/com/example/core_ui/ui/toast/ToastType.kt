package com.example.core_ui.ui.toast

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.core_ui.ui.theme.CardBackground
import com.example.core_ui.ui.theme.DangerOverdue
import com.example.core_ui.ui.theme.SuccessGood
import com.example.core_ui.ui.theme.TeacherPrimary
import com.example.core_ui.ui.theme.TextInverse
import com.example.core_ui.ui.theme.TextPrimary
import com.example.core_ui.ui.theme.WarningPending
import kotlinx.coroutines.delay

enum class ToastType {
    SUCCESS,
    ERROR,
    WARNING,
    INFO,
    DEFAULT
}

@Composable
fun CustomToast(
    message: String,
    type: ToastType,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    duration: Long = 4000
) {
    var visible by remember { mutableStateOf(false) }

    val background = when (type) {
        ToastType.SUCCESS -> SuccessGood
        ToastType.ERROR -> DangerOverdue
        ToastType.WARNING -> WarningPending
        ToastType.INFO -> TeacherPrimary
        ToastType.DEFAULT -> CardBackground
    }

    val textColor = if (type == ToastType.DEFAULT) TextPrimary else TextInverse

    LaunchedEffect(Unit) {
        visible = true
        delay(duration)
        visible = false
        delay(300)
        onDismiss()
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = modifier
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = background),
            elevation = CardDefaults.cardElevation(6.dp),
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = when (type) {
                        ToastType.SUCCESS -> Icons.Default.Check
                        ToastType.ERROR -> Icons.Default.Close
                        ToastType.WARNING -> Icons.Default.Warning
                        ToastType.INFO -> Icons.Default.Info
                        ToastType.DEFAULT -> Icons.Default.Notifications
                    },
                    contentDescription = null,
                    tint = textColor
                )

                Spacer(Modifier.width(12.dp))

                Text(
                    text = message,
                    color = textColor,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}