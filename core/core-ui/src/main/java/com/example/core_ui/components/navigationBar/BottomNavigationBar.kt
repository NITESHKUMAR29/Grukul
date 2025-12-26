package com.example.core_ui.components.navigationBar

import android.util.Log
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.core_ui.components.navigationBar.BottomNavItem
import com.example.core_ui.ui.theme.CardBackground
import com.example.core_ui.ui.theme.TeacherPrimary


@Composable
fun BottomNavigationBar(navController: NavHostController) {

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Classes,
        BottomNavItem.Add,
        BottomNavItem.Students,
        BottomNavItem.Fee
    )

    NavigationBar(
        containerColor = Color.White
    ) {

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route


        items.forEach { item ->

            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    Log.d("BottomNav", "BottomNavigationBar: ${navController.graph.findStartDestination().id}")
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                        tint = if (currentRoute == item.route)
                            CardBackground
                        else
                            TeacherPrimary,
                        modifier = Modifier.size(24.dp)
                    )

                },
                label = { Text(item.title) },

                )
        }
    }
}