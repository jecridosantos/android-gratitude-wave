package com.jdosantos.gratitudewavev1.ui.view.main.notifications

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT
import com.jdosantos.gratitudewavev1.ui.widget.EmptyMessage
import com.jdosantos.gratitudewavev1.ui.widget.Loader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    navController: NavController,
    notificationsViewModel: NotificationsViewModel = hiltViewModel()
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.label_notifications),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                    }

                },
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
            .padding(SPACE_DEFAULT.dp)
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
                EmptyMessage(null, stringResource(R.string.label_no_notifications), null)
            }
        }
    }
}