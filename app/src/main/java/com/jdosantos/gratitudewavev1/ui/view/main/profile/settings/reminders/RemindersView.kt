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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.domain.models.UserSettingReminders
import com.jdosantos.gratitudewavev1.domain.handles.ReminderRepetitions
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT_MID
import com.jdosantos.gratitudewavev1.utils.hourFormat
import com.jdosantos.gratitudewavev1.ui.view.main.profile.settings.SettingsViewModel
import com.jdosantos.gratitudewavev1.ui.widget.EmptyMessage
import com.jdosantos.gratitudewavev1.ui.widget.Loader
import com.jdosantos.gratitudewavev1.utils.getFirstLetters
import com.jdosantos.gratitudewavev1.utils.getRepeatDescription

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemindersView(navController: NavController, settingsViewModel: SettingsViewModel) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.label_reminders),
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
    val configUser by settingsViewModel.userSettings.collectAsState()
    val data = configUser.reminders
    val context = LocalContext.current
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
                    itemsIndexed(data) { index, item ->

                        ItemReminder(item, {
                            navController.navigate("SaveRemindersView/$index")
                        }) {
                            checked ->
                            settingsViewModel.updateReminderState(context, index, checked) {}
                        }


                    }
                }
            } else {
                EmptyMessage(null, stringResource(R.string.label_no_reminders), null)
            }
        }
    }
}

@Composable
private fun ItemReminder(
    userSettingReminders: UserSettingReminders,
    onClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    val repeatConfigSelect = userSettingReminders.repeat
    val selectedDays = userSettingReminders.repeatDays
    var subtitleRepeat = stringResource(id =  getRepeatDescription(repeatConfigSelect))

    if (repeatConfigSelect == ReminderRepetitions.Custom.id && selectedDays!!.size > 0) {
        subtitleRepeat = getFirstLetters(selectedDays, LocalContext.current.resources)
    }
    var isChecked by remember { mutableStateOf(userSettingReminders.active) }
    Card(

        modifier = Modifier
            .fillMaxWidth()
            .padding(top = SPACE_DEFAULT_MID.dp, bottom = SPACE_DEFAULT_MID.dp)
            .clickable { onClick() },
        //    shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(SPACE_DEFAULT.dp),
        ) {

            Column {
                Text(
                    text = hourFormat(userSettingReminders.hour!!, userSettingReminders.minute!!),
                    fontSize = 24.sp
                )
                Text(
                    text = subtitleRepeat,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Switch(isChecked, onCheckedChange = { it ->
                isChecked = it
                onCheckedChange(isChecked)
            })
        }


    }
}