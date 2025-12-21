package com.example.core_ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.core_ui.components.buttons.GurukulPrimaryButton
import com.example.core_ui.ui.theme.GurukulTheme


@Composable
fun SumanjeetScreen() {
    Greeting(name = "Sumanjeet")
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    var loading by remember { mutableStateOf(false) }
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
    Log.d("userIDGet", "SumanjeetScreen: ")

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GurukulTheme {
        Greeting("Sumanjeet")
    }
}