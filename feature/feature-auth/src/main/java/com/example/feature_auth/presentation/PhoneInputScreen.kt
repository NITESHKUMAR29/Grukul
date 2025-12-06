package com.example.feature_auth.presentation

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core_ui.components.buttons.GurukulPrimaryButton
import com.example.core_ui.ui.textFields.CountryCodePicker
import com.example.core_ui.ui.textFields.GurukulTextField
import com.example.core_ui.ui.theme.GurukulTheme
import com.example.feature_auth.R
import kotlinx.coroutines.delay

@Preview(showBackground = true)
@Composable
fun PhoneInputPreview() {
    GurukulTheme {
        PhoneInputScreen(onContinueClick = {})
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhoneInputScreen(onContinueClick: () -> Unit) {


    Log.d("PhoneInputScreen", onContinueClick.toString())


    val pagerState = rememberPagerState(initialPage = 0) {
        3
    }
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000) // 3 seconds
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

        var phone by remember { mutableStateOf("") }
        var countryCode by remember { mutableStateOf("+91") }


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
        Text(text = "Login or Sign up", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(vertical = 8.dp)

        ) {

            CountryCodePicker(
                selectedCode = countryCode,
                onCodeSelected = { countryCode = it },
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(Modifier.width(8.dp))

            GurukulTextField(
                value = phone,
                onValueChange = { phone = it },
                hint = "Enter Mobile number",
                keyboardType = KeyboardType.Phone
            )
        }

        Spacer(Modifier.height(8.dp))
        GurukulPrimaryButton("Continue", onClick = onContinueClick)
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