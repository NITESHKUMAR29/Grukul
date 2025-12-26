package com.example.gurukul.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.core_ui.components.navigationBar.BottomNavItem
import com.example.core_ui.components.navigationBar.BottomNavigationBar
import com.example.core_ui.ui.toast.ToastHost
import com.example.core_ui.ui.toast.ToastState
import com.example.gurukul.splash.SplashViewModel

@Composable
fun GurukulMainScreen() {

    val navController = rememberNavController()
    val viewModel: SplashViewModel = hiltViewModel()
    val destination by viewModel.startDestination.collectAsState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val toastState = remember { ToastState() }

    val bottomBarRoutes = setOf(
        MainRoutes.HOME,
        MainRoutes.CLASSES,
        BottomNavItem.Home.route,
        BottomNavItem.Classes.route,
        BottomNavItem.Students.route,
        BottomNavItem.Fee.route
    )

    val showBottomBar = currentRoute in bottomBarRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController)
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {

            GurukulNavGraph(navController, padding, toastState)

            LaunchedEffect(destination, currentRoute) {
                if (
                    destination != RootRoutes.SPLASH &&
                    currentRoute == RootRoutes.SPLASH
                ) {
                    navController.navigate(destination) {
                        popUpTo(RootRoutes.SPLASH) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }

            if (toastState.currentToast != null) {
                ToastHost(
                    toastState = toastState,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}
