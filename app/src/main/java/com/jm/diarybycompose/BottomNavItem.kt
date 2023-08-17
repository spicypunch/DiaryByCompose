package com.jm.diarybycompose

import com.jm.diarybycompose.R.*

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: Int,
) {
    object Home : BottomNavItem(route = "home", title = "Home", icon = drawable.baseline_home_24)
    object Add : BottomNavItem(route = "like", title = "Like", icon = drawable.baseline_add_box_24)
//    object Calendar : BottomNavItem(route = "calendar", title = "Calendar", icon = drawable.baseline_calendar_month_24)
}
