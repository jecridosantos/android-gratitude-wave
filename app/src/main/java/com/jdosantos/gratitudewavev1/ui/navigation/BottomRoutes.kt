package com.jdosantos.gratitudewavev1.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.jdosantos.gratitudewavev1.R

sealed class BottomRoutes(
    val icon: ImageVector? = null,
    @DrawableRes val icon2: Int,
    @StringRes val title: Int,
    val route: String
) {
    data object HomeView : BottomRoutes(
        null,
        R.drawable.hogar,
        R.string.tab_label_home,
        "HomeView"
    )

    data object ProfileView : BottomRoutes(
        null,
        R.drawable.usuario,
        R.string.tab_label_profile,
        "ProfileView"
    )
}