package com.example.gurukul.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.core_ui.SumanjeetScreen
import com.example.core_ui.components.BottomNavItem
import com.example.core_ui.ui.toast.ToastState
import com.example.core_ui.ui.toast.ToastType
import com.example.feature_auth.presentation.SelectRoleScreen
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

        // ✅ AUTH FLOW (LOGIN, REGISTER...)
        navigation(
            route = RootRoutes.AUTH_GRAPH,
            startDestination = AuthRoutes.LOGIN
        ) {
            composable(AuthRoutes.SELECT_ROLE) {

                val context = LocalContext.current

                SelectRoleScreen(
                    onTeacherClick = {
                        toastState.show("Teacher clicked", ToastType.SUCCESS)


                        navController.navigate(RootRoutes.MAIN_GRAPH) {
                            popUpTo(RootRoutes.AUTH_GRAPH) { inclusive = true }
                        }
                    },

                    onStudentClick = {
                        Toast.makeText(context, "Student clicked", Toast.LENGTH_SHORT).show()

                        navController.navigate(RootRoutes.MAIN_GRAPH) {
                            popUpTo(RootRoutes.AUTH_GRAPH) { inclusive = true }
                        }
                    }
                )
            }
//            composable(AuthRoutes.Login) {
//                LoginScreen(navController)   // implement this
//            }
//
//            composable(AuthRoutes.Register) {
//                RegisterScreen(navController) // implement this
//            }
        }

        // ✅ MAIN APP FLOW (BottomNav Screens)
        navigation(
            route = RootRoutes.MAIN_GRAPH,
            startDestination = MainRoutes.HOME
        ) {
            composable(MainRoutes.HOME) {
                SumanjeetScreen()
            }
            composable(MainRoutes.CLASSES) {
                // ClassesScreen()
            }
            composable(BottomNavItem.Add.route) { }
            composable(BottomNavItem.Students.route) { }
            composable(BottomNavItem.Fee.route) { }
        }
    }
}