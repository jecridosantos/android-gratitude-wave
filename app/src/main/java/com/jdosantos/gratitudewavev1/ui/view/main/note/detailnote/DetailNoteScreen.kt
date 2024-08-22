package com.jdosantos.gratitudewavev1.ui.view.main.note.detailnote

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.domain.models.Note
import com.jdosantos.gratitudewavev1.ui.navigation.Screen
import com.jdosantos.gratitudewavev1.ui.widget.AlertComponent
import com.jdosantos.gratitudewavev1.ui.widget.CurrentDateView
import com.jdosantos.gratitudewavev1.ui.widget.DisplayEmotion
import com.jdosantos.gratitudewavev1.ui.widget.DisplayTag
import com.jdosantos.gratitudewavev1.ui.widget.NoteDropdownMenuItem
import com.jdosantos.gratitudewavev1.ui.widget.getColors
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT_TOP_APP_BAR
import com.jdosantos.gratitudewavev1.utils.getSafeColor

@SuppressLint("UnrememberedMutableState")
@Composable
fun DetailNoteScreen(
    navController: NavController,
    detailNoteViewModel: DetailNoteViewModel = hiltViewModel(),
) {
    val colors = getColors()
    val note = detailNoteViewModel.note

    var selectedColor by remember { mutableStateOf(colors.getSafeColor(detailNoteViewModel.color.toInt())) }


    LaunchedEffect(Unit) {
        detailNoteViewModel.getNoteById(detailNoteViewModel.id) { noteColor ->
            selectedColor = colors.getSafeColor(noteColor)
        }
    }

    Scaffold(
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            selectedColor,

                            )
                    )
                )
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.Start
            ) {
                Header(
                    note = note,
                    onExit = {
                        navController.popBackStack()
                    },
                    onEdit = {
                        navController.navigate(
                            Screen.UpdateNoteScreen.params(
                                detailNoteViewModel.id,
                                note.color!!
                            )
                        )
                    },
                    onDelete = {
                        detailNoteViewModel.showAlert()
                    }, onShare = {
                        navController.navigate(Screen.SharedScreen.params(note.idDoc, note.color!!))
                    }
                )

                Column(
                    modifier = Modifier
                        .padding(SPACE_DEFAULT.dp)
                ) {


                    DisplayNote(note.note)

                    DisplayTag(note.noteTag)

                    DisplayEmotion(note.emotion)

                }


            }
        }
    }

    AlertDeleteNote(detailNoteViewModel.id, detailNoteViewModel, navController)

    DisposableEffect(Unit) {
        onDispose {
            detailNoteViewModel.clean()
        }
    }
}

@Composable
private fun DisplayNote(note: String) {
    Text(
        text = note,
        modifier = Modifier,
        fontSize = 24.sp,
        lineHeight = 35.sp

    )
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun AlertDeleteNote(
    id: String,
    detailNoteViewModel: DetailNoteViewModel,
    navController: NavController
) {
    if (detailNoteViewModel.showAlert) {
        AlertComponent(title = stringResource(id = R.string.note_delete_alert_title),
            message = stringResource(id = R.string.note_delete_alert_message),
            confirmText = stringResource(id = R.string.label_confirm),
            cancelText = stringResource(id = R.string.label_cancel),
            onConfirmClick = {
                detailNoteViewModel.delete(id) {
                    detailNoteViewModel.closeAlert()
                    navController.popBackStack()
                }
            }) {
            detailNoteViewModel.closeAlert()
        }
    }
}

@Composable
private fun Header(
    note: Note,
    onExit: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onShare: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(4.dp)
            .height(SPACE_DEFAULT_TOP_APP_BAR.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onExit() }) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        CurrentDateView(date = note.date, createAt = note.createAt) {}
        Spacer(modifier = Modifier.weight(1f))

        var expanded by remember { mutableStateOf(false) }
        IconButton(
            onClick = { expanded = !expanded }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_more_vert_24),
                contentDescription = "Menú"
            )
            if (expanded) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    NoteDropdownMenuItem(
                        text = "Editar",
                        icon = Icons.Default.Edit,
                        onClick = {
                            onEdit()
                            expanded = false
                        }
                    )
                    NoteDropdownMenuItem(
                        text = "Eliminar",
                        icon = Icons.Default.Delete,
                        onClick = {
                            onDelete()
                            expanded = false
                        }
                    )

                    NoteDropdownMenuItem(
                        text = "Compartir",
                        icon = Icons.Default.Share,
                        onClick = {
                            onShare()
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
