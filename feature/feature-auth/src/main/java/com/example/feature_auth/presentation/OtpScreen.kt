package com.example.feature_auth.presentation

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core_ui.components.buttons.GurukulPrimaryButton
import com.example.core_ui.ui.textFields.CountryCodePicker
import com.example.core_ui.ui.textFields.GurukulTextField
import com.example.core_ui.ui.textFields.OtpView
import com.example.core_ui.ui.theme.GurukulTheme
import com.example.core_ui.ui.theme.TeacherPrimary
import com.example.core_ui.ui.theme.TextPrimary
import com.example.core_ui.ui.theme.TextSecondary
import com.example.feature_auth.R
import kotlinx.coroutines.delay

@Preview(showBackground = true)
@Composable
fun OtpScreenPreview() {
    GurukulTheme {
        OtpScreen(onOtpVerified = {})
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OtpScreen(onOtpVerified: () -> Unit) {

    var navigated by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState(initialPage = 0) {
        3
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount
            pagerState.animateScrollToPage(nextPage)
        }
    }

    val images = listOf(
        R.drawable.img,
        R.drawable.img,
        R.drawable.img,
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                Image(
                    painter = painterResource(images[page]),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                repeat(images.size) { index ->
                    IndicatorDot(pagerState.currentPage == index)
                }
            }
        }
        Spacer(Modifier.height(32.dp))
        Text(text = "Enter OTP", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(12.dp))

        OtpView { otp ->
            if (otp.length == 6 && !navigated) {
                navigated = true
                onOtpVerified()
            }
        }
        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "OTP sent on",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
            Spacer(Modifier.width(5.dp))
            Text(
                text = "+91 6290438875",
                style = MaterialTheme.typography.labelSmall,
                color = TextPrimary
            )
        }
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Retry sending OTP in",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
            Spacer(Modifier.width(5.dp))
            Text(
                text = "59 seconds ",
                style = MaterialTheme.typography.labelSmall,
                color = TeacherPrimary
            )
        }




        Spacer(Modifier.height(40.dp))
    }


}


