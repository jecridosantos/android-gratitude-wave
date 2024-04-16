package com.jdosantos.gratitudewavev1.ui.view.main.profile.settings.reminders

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.core.common.util.getWeekDaysByLocale

@SuppressLint("MutableCollectionMutableState")
@Composable
fun DaysOfWeekDialog(
    visible: Boolean,
    selectedDays: MutableList<Int>,
    onDismiss: () -> Unit,
    onConfirm: (MutableList<Int>) -> Unit
) {
    if (visible) {
        val daysOfWeek = getWeekDaysByLocale()
        val updatedSelection = remember { mutableStateListOf<Int>() }
        LaunchedEffect(selectedDays) {
            updatedSelection.clear()
            updatedSelection.addAll(selectedDays)
        }
        AlertDialog(
            title = { Text(stringResource(id = R.string.label_repeat)) },
            text = {
                LazyColumn {
                    itemsIndexed(daysOfWeek) { index, day ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val isSelected = updatedSelection.contains(index)
                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        updatedSelection.add(index)
                                    } else {
                                        updatedSelection.remove(index)
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = day)
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    onConfirm(updatedSelection)
                    onDismiss()
                }) {
                    Text(stringResource(id = R.string.label_confirm))
                }
            },
            dismissButton = {
                Button(onClick = { onDismiss() }) {
                    Text(stringResource(id = R.string.label_cancel))
                }
            },
            onDismissRequest = { }
        )
    }
}