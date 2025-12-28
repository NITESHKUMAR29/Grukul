package com.example.gurukul.navigation

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.core_ui.SumanjeetScreen
import com.example.core_ui.components.navigationBar.BottomNavItem
import com.example.core_ui.ui.toast.ToastState
import com.example.feature_auth.presentation.AuthViewModel
import com.example.feature_auth.presentation.NameInputScreen
import com.example.feature_auth.presentation.OtpScreen
import com.example.feature_auth.presentation.PhoneInputScreen
import com.example.feature_auth.presentation.SelectRoleScreen
import com.example.feature_class.presentation.ClassScreen
import com.example.feature_home.presentation.FormScreenHost
import com.example.gurukul.splash.SplashScreen

@Composable
fun GurukulNavGraph(
    navController: NavHostController,
    padding: PaddingValues,
    toastState: ToastState
) {
    NavHost(
        navController = navController,
        startDestination = RootRoutes.SPLASH,
        modifier = Modifier.padding(padding)
    ) {

        // ✅ SPLASH
        composable(RootRoutes.SPLASH) {
            SplashScreen()
        }


        navigation(
            route = RootRoutes.AUTH_GRAPH,
            startDestination = AuthRoutes.SELECT_ROLE
        ) {
            composable(AuthRoutes.SELECT_ROLE) {


                SelectRoleScreen(
                    onTeacherClick = {
                        navController.navigate(AuthRoutes.PHONE_INPUT)
                    },

                    onStudentClick = {
                        navController.navigate(RootRoutes.MAIN_GRAPH) {
                            popUpTo(RootRoutes.AUTH_GRAPH) { inclusive = true }
                        }
                    }
                )
            }

            composable(AuthRoutes.PHONE_INPUT) { entry ->

                val parentEntry = remember(entry) {
                    navController.getBackStackEntry(RootRoutes.AUTH_GRAPH)
                }

                val authViewModel = hiltViewModel<AuthViewModel>(parentEntry)

                PhoneInputScreen(
                    viewModel = authViewModel,
                    toastState,
                    onNavigateOtp = {
                        navController.navigate(AuthRoutes.OTP_INPUT)
                    }
                )
            }


            composable(AuthRoutes.NAME_INPUT) { entry ->

                val parentEntry = remember(entry) {
                    navController.getBackStackEntry(RootRoutes.AUTH_GRAPH)
                }

                val authViewModel = hiltViewModel<AuthViewModel>(parentEntry)

                NameInputScreen(
                    viewModel = authViewModel,
                    onContinueClick = {
                        navController.navigate(RootRoutes.MAIN_GRAPH) {
                            popUpTo(RootRoutes.AUTH_GRAPH) { inclusive = true }
                        }
                    }
                )
            }

            composable(AuthRoutes.OTP_INPUT) { entry ->

                val parentEntry = remember(entry) {
                    navController.getBackStackEntry(RootRoutes.AUTH_GRAPH)
                }

                val authViewModel = hiltViewModel<AuthViewModel>(parentEntry)

                OtpScreen(
                    viewModel = authViewModel,
                    onGoToMain = {
                        navController.navigate(RootRoutes.MAIN_GRAPH) {
                            popUpTo(RootRoutes.AUTH_GRAPH) { inclusive = true }
                        }
                    },
                    onGoToName = {
                        navController.navigate(AuthRoutes.NAME_INPUT)
                    }
                )
            }

        }

        navigation(
            route = RootRoutes.MAIN_GRAPH,
            startDestination = MainRoutes.HOME
        ) {
            composable(MainRoutes.HOME) {
                SumanjeetScreen()
            }
            composable(MainRoutes.CLASSES) {
                ClassScreen()
            }
            composable(BottomNavItem.Add.route) {
                FormScreenHost(
                    onBack = {
                        Log.d("onBackClicked","onbackClicked")
                        navController.popBackStack()
                    }
                )
            }
            composable(BottomNavItem.Students.route) { }
            composable(BottomNavItem.Fee.route) { }
        }
    }
}