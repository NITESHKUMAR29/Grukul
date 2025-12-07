package com.example.feature_auth.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.core_ui.ui.textFields.OtpView
import com.example.core_ui.ui.theme.TeacherPrimary
import com.example.core_ui.ui.theme.TextPrimary
import com.example.core_ui.ui.theme.TextSecondary
import com.example.feature_auth.R
import kotlinx.coroutines.delay


@OptIn(ExperimentalFoundationApi::class)

@Composable
fun OtpScreen(
    viewModel: AuthViewModel,
    onGoToMain: () -> Unit,
    onGoToName: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val pagerState = rememberPagerState { 3 }

    var consumed by remember { mutableStateOf(false) }
    var remainingTime by remember { mutableStateOf(60) }
    var canResend by remember { mutableStateOf(false) }

    // Countdown timer
    LaunchedEffect(canResend) {
        if (!canResend) {
            remainingTime = 60
            while (remainingTime > 0) {
                delay(1000)
                remainingTime--
            }
            canResend = true
        }
    }

    // Observe auth state
    LaunchedEffect(state) {

        if (consumed) return@LaunchedEffect

        when (state) {

            is AuthState.Success -> {
                consumed = true
                viewModel.resetStateOnly()

                val user = (state as AuthState.Success).user
                if (user.role.name == "UNKNOWN") onGoToName() else onGoToMain()
            }

            is AuthState.Error -> {
                viewModel.resetStateOnly()
            }

            else -> Unit
        }
    }

    // Banner animation
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            val next = (pagerState.currentPage + 1) % pagerState.pageCount
            pagerState.animateScrollToPage(next)
        }
    }

    val images = listOf(R.drawable.img, R.drawable.img, R.drawable.img)

    Column(
        Modifier.fillMaxSize(),
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
        Text("Enter OTP", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(12.dp))

        OtpView { otp ->
            if (otp.length == 6) viewModel.verifyOtp(otp)
        }

        Spacer(Modifier.height(16.dp))

        if (!canResend) {
            Text("Retry in $remainingTime sec", color = TeacherPrimary)
        } else {
            Text(
                "Resend OTP",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clickable {
                        consumed = false
                        canResend = false
                        viewModel.resendOtp()
                    }
                    .padding(8.dp)
            )
        }

        Spacer(Modifier.height(20.dp))

        Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            Text("OTP sent to", color = TextSecondary)
            Spacer(Modifier.width(6.dp))
            Text(viewModel.phoneNumber.orEmpty(), color = TextPrimary)
        }

        Spacer(Modifier.height(40.dp))
    }
}

