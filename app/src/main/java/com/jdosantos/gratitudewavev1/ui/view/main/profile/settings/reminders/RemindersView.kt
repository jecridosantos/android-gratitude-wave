package com.jdosantos.gratitudewavev1.ui.view.main.profile.settings.reminders

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jdosantos.gratitudewavev1.app.enums.getFirstLetters
import com.jdosantos.gratitudewavev1.app.model.ConfigUserReminder
import com.jdosantos.gratitudewavev1.core.common.util.hourFormat
import com.jdosantos.gratitudewavev1.core.common.util.repeatListOptions
import com.jdosantos.gratitudewavev1.ui.view.main.profile.settings.SettingsViewModel
import com.jdosantos.gratitudewavev1.ui.widget.EmptyMessage
import com.jdosantos.gratitudewavev1.ui.widget.Loader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemindersView(navController: NavController, settingsViewModel: SettingsViewModel) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Recordatorios",
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
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("SaveRemindersView/${-1}")
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    modifier = Modifier.size(24.dp),
                    contentDescription = ""
                )
            }
        }


    ) { paddingValues ->
        ContentRemindersView(paddingValues, navController, settingsViewModel)
    }

}

@Composable
private fun ContentRemindersView(
    paddingValues: PaddingValues,
    navController: NavController,
    settingsViewModel: SettingsViewModel
) {
    val isLoading by settingsViewModel.isLoading.collectAsState()
    // val data by settingsViewModel.reminders.collectAsState()
    val configUser by settingsViewModel.configUser.collectAsState()
    val data = configUser.reminders
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
                    itemsIndexed(data) {index, item ->

                        ItemReminder(item, {
                            navController.navigate("SaveRemindersView/$index")
                        }) {
                        settingsViewModel.updateReminderState(index, it)
                        }
                    }
                }
            } else {
                EmptyMessage(null, "Sin recordatorios", null)
            }
        }
    }
}

@Composable
private fun ItemReminder(
    configUserReminder: ConfigUserReminder,
    onClick: ()-> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    val repeatConfigSelect = configUserReminder.repeat
    val selectedDays = configUserReminder.repeatDays
    var subtitleRepeat = stringResource(id = repeatListOptions[repeatConfigSelect].title)

    if (repeatConfigSelect == 3 && selectedDays!!.size > 0) {
        subtitleRepeat = getFirstLetters(selectedDays)
    }
    var isChecked by remember { mutableStateOf(configUserReminder.active) }
    Card(

        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp)
            .clickable { onClick() },
        //    shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
        ) {

            Column {
                Text(
                    text = hourFormat(configUserReminder.hour!!, configUserReminder.minute!!),
                    fontSize = 24.sp
                )
                Text(
                    text = subtitleRepeat,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Switch(isChecked, onCheckedChange = {
                it ->
                isChecked = it
                onCheckedChange(isChecked)
            })
        }


    }
}