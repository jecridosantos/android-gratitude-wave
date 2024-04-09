package com.jdosantos.gratitudewavev1.ui.view.main.profile.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jdosantos.gratitudewavev1.core.common.constants.Constants.Companion.HEIGHT_ITEMS_CONFIG
import com.jdosantos.gratitudewavev1.ui.widget.ConfigItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsView(navController: NavController, settingsViewModel: SettingsViewModel) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Settings",
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                    }

                }
            )
        }

    ) { paddingValues ->
        ContentSettingsView(paddingValues, navController, settingsViewModel)
    }

}

@Composable
private fun ContentSettingsView(
    paddingValues: PaddingValues,
    navController: NavController,
    settingsViewModel: SettingsViewModel
) {
    val configUser by settingsViewModel.configUser.collectAsState()

    Column(modifier = Modifier.padding(paddingValues)) {

        SettingItemCheck(title = "Silenciar notificaciones", configUser.muteNotifications) {
            settingsViewModel.saveMuteNotifications(it)
        }
        SettingItem(title = "Personalizar recordatorios") {
            navController.navigate("RemindersView")
        }
    }
}

@Composable
private fun SettingItemCheck(
    title: String,
    isCheckedInitially: Boolean = false,
    changeCheck: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
            .height(HEIGHT_ITEMS_CONFIG.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, modifier = Modifier.weight(1f))
        var isChecked by remember { mutableStateOf(isCheckedInitially) }
        Switch(
            checked = isChecked,
            onCheckedChange = {
                isChecked = it
                changeCheck(isChecked)
            }
        )
    }
}

@Composable
private fun SettingItem(title: String, onClick: () -> Unit) {
    ConfigItem(onClick) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.outline
            )


        }

    }

}