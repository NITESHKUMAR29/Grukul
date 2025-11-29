package com.example.gurukul.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.core_ui.SumanjeetScreen
import com.example.core_ui.components.BottomNavItem

@Composable
fun GurukulNavGraph(
    navController: NavHostController,
    padding: PaddingValues
) {
    NavHost(
        navController,
        startDestination = BottomNavItem.Home.route,
        modifier = Modifier.padding(padding)
    ) {
        composable(BottomNavItem.Home.route) { SumanjeetScreen() }
        composable(BottomNavItem.Classes.route) {  }
        composable(BottomNavItem.Add.route) {  }
        composable(BottomNavItem.Students.route) {  }
        composable(BottomNavItem.Fee.route) {  }
    }

}