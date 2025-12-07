package com.example.feature_auth.presentation

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.core_ui.components.buttons.GurukulPrimaryButton
import com.example.core_ui.ui.textFields.CountryCodePicker
import com.example.core_ui.ui.textFields.GurukulTextField
import com.example.core_ui.ui.toast.ToastState
import com.example.core_ui.ui.toast.ToastType
import com.example.feature_auth.R
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhoneInputScreen(
    viewModel: AuthViewModel,
    toastState: ToastState,
    onNavigateOtp: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        Log.d("PhoneInputScreenState", "State: $state")

        when (state) {

            is AuthState.CodeSent -> {
                isLoading = false
                viewModel.resetStateOnly()
                onNavigateOtp()
            }

            is AuthState.Loading -> {
                isLoading = true
            }

            is AuthState.Error -> {
                toastState.show((state as AuthState.Error).message, ToastType.ERROR)
                isLoading = false
                viewModel.resetStateOnly()
            }

            else -> Unit
        }
    }

    val pagerState = rememberPagerState { 3 }

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            val next = (pagerState.currentPage + 1) % pagerState.pageCount
            pagerState.animateScrollToPage(next)
        }
    }

    val images = listOf(R.drawable.img, R.drawable.img, R.drawable.img)

    var phone by remember { mutableStateOf("") }
    var countryCode by remember { mutableStateOf("+91") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Box(Modifier.fillMaxWidth().weight(1f)) {
            HorizontalPager(state = pagerState) { page ->
                Image(
                    painter = painterResource(images[page]),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Row(
                Modifier.align(Alignment.BottomEnd).padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                repeat(images.size) {
                    IndicatorDot(pagerState.currentPage == it)
                }
            }
        }

        Spacer(Modifier.height(32.dp))
        Text("Login or Sign up", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(8.dp))


        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CountryCodePicker(
                selectedCode = countryCode,
                onCodeSelected = { countryCode = it },
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(Modifier.width(8.dp))

            GurukulTextField(
                value = phone,
                onValueChange = { phone = it.filter(Char::isDigit) },
                hint = "Enter mobile number",
                keyboardType = KeyboardType.Phone
            )
        }

        Spacer(Modifier.height(12.dp))

        GurukulPrimaryButton(
            text = "Continue",
            loading = isLoading,
            onClick = {

                val trimmed = phone.trim()

                when {
                    trimmed.isEmpty() ->
                        toastState.show("Enter mobile number", ToastType.ERROR)

                    trimmed.length != 10 ->
                        toastState.show("Enter valid 10-digit number", ToastType.ERROR)

                    else -> {
                        val full = "$countryCode$trimmed"
                        if (full==viewModel.phoneNumber){
                            onNavigateOtp()
                            return@GurukulPrimaryButton
                        }
                        viewModel.sendOtp(context as Activity, full)
                    }
                }
            }
        )

        Spacer(Modifier.height(32.dp))
    }
}


@Composable
fun IndicatorDot(isSelected: Boolean) {
    Box(
        modifier = Modifier
            .size(if (isSelected) 10.dp else 8.dp)
            .clip(CircleShape)
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary
                else Color.White.copy(alpha = 0.6f)
            )
    )
}