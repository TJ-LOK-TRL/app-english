package com.masterproject.englishapp.navigation

import androidx.annotation.DrawableRes
import com.masterproject.englishapp.R

sealed class BottomNavItem(
    val screen: Screen,
    val title: String,
    @DrawableRes val iconRes: Int
) {
    object Practice : BottomNavItem(
        screen = Screen.HOME,
        title = "Practice",
        iconRes = R.drawable.ic_graduation
    )

    object Lessons : BottomNavItem(
        screen = Screen.VIDEOS,
        title = "Lessons",
        iconRes = R.drawable.ic_lessons
    )

    object More : BottomNavItem(
        screen = Screen.PRACTICE,
        title = "More",
        iconRes = R.drawable.ic_video
    )

    object Profile : BottomNavItem(
        screen = Screen.ACCOUNT,
        title = "Profile",
        iconRes = R.drawable.ic_profile
    )
}