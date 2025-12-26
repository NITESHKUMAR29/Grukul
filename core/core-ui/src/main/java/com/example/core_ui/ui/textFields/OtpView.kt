package com.example.core_ui.ui.textFields

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core_ui.ui.theme.GurukulTheme

@Preview
@Composable
fun OtpViewPreview() {
    GurukulTheme {
        OtpView()
    }
}

@Composable
fun OtpView(
    otpLength: Int = 6,
    onOtpChange: (String) -> Unit = {}
) {
    var otp by remember { mutableStateOf("") }
    val focusRequesters = remember { List(otpLength) { FocusRequester() } }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(otpLength) { index ->
            OutlinedTextField(
                value = otp.getOrNull(index)?.toString() ?: "",
                onValueChange = { newValue ->
                    if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                        otp = buildString {
                            append(otp.take(index))
                            append(newValue)
                            if (otp.length > index + 1) append(otp.drop(index + 1))
                        }.take(otpLength)

                        onOtpChange(otp)

                        // Auto move to next
                        if (newValue.isNotEmpty() && index < otpLength - 1) {
                            focusRequesters[index + 1].requestFocus()
                        }
                        // Auto move back on delete
                        if (newValue.isEmpty() && index > 0) {
                            focusRequesters[index - 1].requestFocus()
                        }
                    }
                },
                modifier = Modifier
                    .weight(1F)
                    .height(56.dp)
                    .focusRequester(focusRequesters[index]),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    textAlign = TextAlign.Center
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFE5E7EB),
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary,
                ),
                shape = RoundedCornerShape(6.dp)
            )
        }
    }
}