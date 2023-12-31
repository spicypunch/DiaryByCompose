package com.jm.diarybycompose.data.domain.model

import com.jm.diarybycompose.R

sealed class MenuItem(
    val title: String,
    val route: String,
    val selectedIcon: Int,
    val unselectedIcon: Int
) {
    object Notification : MenuItem(
        title = "Notification",
        route = "notification",
        selectedIcon = R.drawable.baseline_notifications_24,
        unselectedIcon = R.drawable.outline_notifications_24
    )

    object Setting : MenuItem(
        title = "Setting",
        route = "setting",
        selectedIcon = R.drawable.baseline_settings_24,
        unselectedIcon = R.drawable.outline_settings_24
    )
}
