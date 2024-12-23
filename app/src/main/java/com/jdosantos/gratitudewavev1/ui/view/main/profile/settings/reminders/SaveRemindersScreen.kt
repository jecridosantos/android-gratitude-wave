package com.jdosantos.gratitudewavev1.ui.view.main.profile.settings.reminders

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.domain.handles.ReminderRepetitions
import com.jdosantos.gratitudewavev1.ui.view.main.profile.settings.SettingsViewModel
import com.jdosantos.gratitudewavev1.ui.widget.ConfigItem
import com.jdosantos.gratitudewavev1.ui.widget.InputRound
import com.jdosantos.gratitudewavev1.ui.widget.ItemSelectedOptions
import com.jdosantos.gratitudewavev1.ui.widget.TimePickerDialog
import com.jdosantos.gratitudewavev1.utils.checkSelectedDays
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.REMINDER_INDEX_EMPTY
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT
import com.jdosantos.gratitudewavev1.utils.getFirstLetters
import com.jdosantos.gratitudewavev1.utils.getRepeatDescription
import com.jdosantos.gratitudewavev1.utils.hourFormat
import com.jdosantos.gratitudewavev1.utils.repeatListOptions

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveRemindersScreen(
    id: Int,
    hour: Int,
    minute: Int,
    navController: NavController,
    settingsViewModel: SettingsViewModel
) {
    val currentReminder = settingsViewModel.currentReminder
    val context = LocalContext.current

    val isUpdated = id != REMINDER_INDEX_EMPTY
    LaunchedEffect(Unit) {
        if (isUpdated) {
            settingsViewModel.fillReminder(id)
        }
    }
    settingsViewModel.toastMessage.observe(LocalLifecycleOwner.current, Observer { message ->
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    })
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (isUpdated) stringResource(R.string.label_update_reminders) else stringResource(
                            R.string.label_save_reminders
                        ),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = ""
                        )
                    }

                },
                actions = {

                    if (isUpdated) {
                        IconButton(onClick = {
                            settingsViewModel.deleteReminder(id)
                            navController.popBackStack()
                        }

                        ) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "")
                        }

                    }
                    IconButton(onClick = {
                        if (isUpdated) {
                            settingsViewModel.updateReminder(id)
                        } else {
                            settingsViewModel.addReminder()
                        }
                        navController.popBackStack()

                    }) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = "")
                    }
                }
            )
        }

    ) { paddingValues ->

        val timePickerState = rememberTimePickerState(
            initialHour = hour,
            initialMinute = minute
        )

        val showTimePickerDialog = rememberSaveable { mutableStateOf(false) }
        val showRepeatDialog = rememberSaveable { mutableStateOf(false) }
        val showRepeatSelectDayDialog = rememberSaveable { mutableStateOf(false) }


        // https://material.io/blog/material-3-compose-1-1
        if (showTimePickerDialog.value) {
            TimePickerDialog(
                title = stringResource(R.string.label_select_time),

                onDismissRequest = { showTimePickerDialog.value = false },
                confirmButton = {
                    TextButton(onClick = {
                        showTimePickerDialog.value = false
                        settingsViewModel.fillReminderHour(
                            timePickerState.hour,
                            timePickerState.minute
                        )
                    }) {
                        Text(stringResource(id = R.string.label_confirm))
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showTimePickerDialog.value = false
                    }) {
                        Text(stringResource(id = R.string.label_cancel))
                    }
                }) {
                TimePicker(state = timePickerState)
            }
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
        ) {

            ItemReminderOption(
                title = stringResource(R.string.label_time),
                config = hourFormat(currentReminder.hour!!, currentReminder.minute!!)
            ) {
                showTimePickerDialog.value = true
            }

            var subtitleRepeat = stringResource(id = getRepeatDescription(currentReminder.repeat))

            if (currentReminder.repeat == ReminderRepetitions.Custom.id && currentReminder.repeatDays!!.size > 0) {
                subtitleRepeat =
                    getFirstLetters(currentReminder.repeatDays, LocalContext.current.resources)
            }

            ItemReminderOption(
                title = stringResource(R.string.label_repeat),
                config = subtitleRepeat
            ) {
                showRepeatDialog.value = true
            }

            Spacer(modifier = Modifier.height(16.dp))
            InputRound(
                stringResource(R.string.label_label),
                currentReminder.label!!,
                stringResource(R.string.label_my_morning_reminder),
                KeyboardType.Text,
                imeAction = ImeAction.Done
            ) {
                settingsViewModel.fillReminderLabel(it)
            }

        }

        ChooseRepeat(
            selected = currentReminder.repeat,
            isOpen = showRepeatDialog.value,
            onHide = {
                showRepeatDialog.value = false
            },
            onSelected = {
                showRepeatDialog.value = false
                if (it!!.id == ReminderRepetitions.Custom.id) {
                    showRepeatSelectDayDialog.value = true
                }
                settingsViewModel.fillReminderRepeat(it.id)
            })

        DaysOfWeekDialog(
            visible = showRepeatSelectDayDialog.value,
            selectedDays = currentReminder.repeatDays!!,
            onDismiss = { showRepeatSelectDayDialog.value = false },
            onConfirm = {
                settingsViewModel.fillReminderRepeatDays(it.toMutableList())
                val (allDaysSelected, weekdaysSelected) = checkSelectedDays(it.toMutableList())

                if (allDaysSelected) {
                    settingsViewModel.fillReminderRepeat(ReminderRepetitions.Daily.id)
                }

                if (weekdaysSelected) {
                    settingsViewModel.fillReminderRepeat(ReminderRepetitions.MonToFri.id)
                }

                showRepeatSelectDayDialog.value = false


            }
        )
    }
    DisposableEffect(Unit) {
        onDispose {
            settingsViewModel.cleanReminder()
        }
    }
}

@Composable
private fun ItemReminderOption(title: String, config: String, onClick: () -> Unit) {
    ConfigItem(onClick) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))

            Row {
                Text(text = config, color = MaterialTheme.colorScheme.outline)
                Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChooseRepeat(
    selected: Int?,
    isOpen: Boolean,
    onHide: () -> Unit,
    onSelected: (ReminderRepetitions?) -> Unit
) {
    if (isOpen) {
        val items: List<ReminderRepetitions> = repeatListOptions
        ModalBottomSheet(onDismissRequest = { onHide() }) {

            Column(
                modifier = Modifier.padding(SPACE_DEFAULT.dp)
            ) {
                TitleRepeatModal()
                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn {
                    itemsIndexed(items) { _, item ->

                        ItemSelectedOptions(selected == item.id, stringResource(id = item.title)) {
                            onSelected(item)
                        }

                    }
                }
                Spacer(modifier = Modifier.width(16.dp))

            }
        }
    }
}

@Composable
private fun TitleRepeatModal() {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(id = R.string.label_label),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
}
