package com.jdosantos.gratitudewavev1.ui.view.main.profile.settings.reminders.save

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@SuppressLint("MutableCollectionMutableState")
@Composable
fun DaysOfWeekDialog(
    visible: Boolean,
    selectedDays: MutableList<Int>,
    onDismiss: () -> Unit,
    onConfirm: (MutableList<Int>) -> Unit
) {
    if (visible) {
        val daysOfWeek = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
        val updatedSelection = remember { mutableStateListOf<Int>() }
        LaunchedEffect(selectedDays) {
            updatedSelection.clear()
            updatedSelection.addAll(selectedDays)
        }
        AlertDialog(
            title = { Text("Repetir") },
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
                    Text("Aceptar")
                }
            },
            dismissButton = {
                Button(onClick = { onDismiss() }) {
                    Text("Cancelar")
                }
            },
            onDismissRequest = { }
        )
    }
}