package com.example.core_ui.components.navigationBar

import com.example.core_ui.R

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: Int
) {
    object Home : BottomNavItem("home", "Home", R.drawable.baseline_home_24)
    object Classes : BottomNavItem("classes", "Classes", R.drawable.classes_icon)


    object Add : BottomNavItem(
        "add_class",
        "Add",
        R.drawable.add_icon
    )

    object Students : BottomNavItem("students", "Students", R.drawable.students_icon)
    object Fee : BottomNavItem("fee", "Fee", R.drawable.fee_icon)
}
