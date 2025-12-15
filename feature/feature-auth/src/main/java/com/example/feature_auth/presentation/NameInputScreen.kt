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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core_model.models.User
import com.example.core_model.models.UserRole
import com.example.core_ui.components.buttons.GurukulPrimaryButton
import com.example.core_ui.ui.textFields.GurukulTextField
import com.example.core_ui.ui.theme.GurukulTheme
import com.example.feature_auth.R
import kotlinx.coroutines.delay

//@Preview(showBackground = true)
//@Composable
//fun NameInputScreenPreview() {
//    GurukulTheme {
//        NameInputScreen(onContinueClick = {}, viewModel = AuthViewModel)
//    }
//}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NameInputScreen(onContinueClick: () -> Unit, viewModel: AuthViewModel) {

    var name by remember { mutableStateOf("") }
    val state by viewModel.state.collectAsState()


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
        Text(text = "Enter Your Name", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(8.dp))
        GurukulTextField(
            value = name,
            onValueChange = { name = it },
            hint = "Your full name",
            keyboardType = KeyboardType.Text,
            modifier = Modifier.padding(horizontal = 16.dp)
        )


        Spacer(Modifier.height(8.dp))
        GurukulPrimaryButton(
            text = "Register",
            onClick = {
                Log.d("Namesss",name)
                if (name.isNotBlank()) {

                    viewModel.saveUser(
                        name = name,
                        role = UserRole.TEACHER
                    )
                }
            }
        )

        LaunchedEffect(state) {
            Log.d("NameInputScreen", "State: $state")
            if (state is AuthState.Success) {
                viewModel.resetStateOnly()
                onContinueClick()
            }
        }

        Spacer(Modifier.height(32.dp))
    }


}


