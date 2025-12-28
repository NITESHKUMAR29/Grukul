package com.example.gurukul.navigation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.core_ui.components.navigationBar.BottomNavItem
import com.example.core_ui.components.navigationBar.BottomNavigationBar
import com.example.core_ui.components.toolBars.GurukulTopBar
import com.example.core_ui.ui.toast.ToastHost
import com.example.core_ui.ui.toast.ToastState
import com.example.gurukul.splash.SplashViewModel

@Composable
fun GurukulMainScreen() {

    val navController = rememberNavController()
    val viewModel: SplashViewModel = hiltViewModel()
    val destination by viewModel.startDestination.collectAsState()
    var hasNavigated by rememberSaveable { mutableStateOf(false) }
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
        topBar = {
            if (showBottomBar) { GurukulTopBar(
                title = "Namaste"
            )}

        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController)
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {

            GurukulNavGraph(navController, padding, toastState)

            LaunchedEffect(destination, navBackStackEntry) {
                Log.d("NAV_DEBUG", "destination = $destination")
                if (
                    !hasNavigated &&
                    navBackStackEntry != null &&
                    destination != RootRoutes.SPLASH
                ) {
                    hasNavigated = true
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
