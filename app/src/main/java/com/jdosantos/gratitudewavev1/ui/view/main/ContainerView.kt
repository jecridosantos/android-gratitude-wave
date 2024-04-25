package com.jdosantos.gratitudewavev1.ui.view.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jdosantos.gratitudewavev1.ui.navigation.BottomRoutes
import com.jdosantos.gratitudewavev1.ui.view.main.home.HomeView
import com.jdosantos.gratitudewavev1.ui.view.main.home.HomeViewModel
import com.jdosantos.gratitudewavev1.ui.view.main.profile.ProfileView
import com.jdosantos.gratitudewavev1.ui.view.main.profile.ProfileViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ContainerView(
    mainNavController: NavController,
    profileViewModel: ProfileViewModel,
    homeViewModel: HomeViewModel
) {
    val navHostController = rememberNavController()
    val navigationRoutes = listOf(
        BottomRoutes.HomeView,
        BottomRoutes.ProfileView
    )

    Scaffold(bottomBar = {

        NavigationBar(
            containerColor = MaterialTheme.colorScheme.background,
        ) {
            val currentRoute = currentRoute(navHostController = navHostController)

            navigationRoutes.forEach {
                NavigationBarItem(
                    selected = currentRoute == it.route,
                    onClick = { navHostController.navigate(it.route) },
                    icon = {
                        if (it.icon != null) {
                            Icon(
                                imageVector = it.icon,
                                modifier = Modifier.size(24.dp),
                                contentDescription = stringResource(id = it.title)
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = it.icon2),
                                modifier = Modifier.size(24.dp),
                                contentDescription = stringResource(id = it.title)
                            )
                        }
                    },
                    label = { Text(text = stringResource(id = it.title)) },
                    alwaysShowLabel = true
                )
            }
        }

    }
    ) {
        NavHost(
            navHostController,
            startDestination = navigationRoutes[0].route,
            Modifier.padding(it)
        ) {
            composable(navigationRoutes[0].route) { HomeView(mainNavController, homeViewModel) }
            composable(navigationRoutes[1].route) {
                ProfileView(
                    profileViewModel,
                    mainNavController
                )
            }
        }
    }

}

@Composable
fun currentRoute(navHostController: NavHostController): String? {
    val current by navHostController.currentBackStackEntryAsState()
    return current?.destination?.route
}

