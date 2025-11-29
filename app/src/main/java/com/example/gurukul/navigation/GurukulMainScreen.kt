package com.example.gurukul.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.core_ui.components.BottomNavigationBar

@Composable
fun GurukulMainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { padding ->
        GurukulNavGraph(navController, padding)
    }
}