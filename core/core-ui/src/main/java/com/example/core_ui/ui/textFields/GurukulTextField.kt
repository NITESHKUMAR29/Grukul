package com.example.core_ui.ui.textFields

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.core_ui.ui.theme.DangerOverdue
import com.example.core_ui.ui.theme.TeacherPrimary
import com.example.core_ui.ui.theme.TextDisabled
import com.example.core_ui.ui.theme.TextPrimary

@Composable
fun GurukulTextField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = hint,
                style = MaterialTheme.typography.bodySmall,
                color = TextDisabled
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 52.dp),
        enabled = enabled,
        singleLine = singleLine,
        isError = isError,
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color(0xFFE5E7EB),
            focusedBorderColor = TeacherPrimary,
            cursorColor = TeacherPrimary,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            disabledBorderColor = Color(0xFFCBD5E1),
            errorBorderColor = DangerOverdue
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}
