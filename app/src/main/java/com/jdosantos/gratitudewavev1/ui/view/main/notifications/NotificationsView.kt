package com.jdosantos.gratitudewavev1.ui.view.main.notifications

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jdosantos.gratitudewavev1.ui.widget.EmptyMessage
import com.jdosantos.gratitudewavev1.ui.widget.Loader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsView(
    navController: NavController,
    notificationsViewModel: NotificationsViewModel
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Notifications",
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                    }

                },
  /*              actions = {
                    IconButton(onClick = { *//*TODO*//* }) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = "")
                    }
                }*/
            )
        }

    ) { paddingValues ->
        ContentNotificationsView(paddingValues, navController, notificationsViewModel)
    }

}

@Composable
fun ContentNotificationsView(
    paddingValues: PaddingValues,
    navController: NavController,
    notificationsViewModel: NotificationsViewModel
) {
    val isLoading by notificationsViewModel.isLoading.collectAsState()
    val data by notificationsViewModel.data.collectAsState()

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        if (isLoading) {
            Loader()
        } else {
            if (data.isNotEmpty()) {
                LazyColumn {
                    items(data) {
                    }
                }
            } else {
                EmptyMessage(null, "Sin notificaciones", null)
            }
        }
    }
}