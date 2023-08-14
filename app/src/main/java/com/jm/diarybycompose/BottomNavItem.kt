package com.jm.diarybycompose

import com.example.diarybycompose.R

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: Int,
) {
    object Home : BottomNavItem(route = "home", title = "Home", icon = R.drawable.baseline_home_24)
    object Add : BottomNavItem(route = "like", title = "Like", icon = R.drawable.baseline_add_box_24)
}
