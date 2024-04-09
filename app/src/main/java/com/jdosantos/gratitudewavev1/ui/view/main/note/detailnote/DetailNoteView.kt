package com.jdosantos.gratitudewavev1.ui.view.main.note.detailnote

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.core.common.util.getSafeColor
import com.jdosantos.gratitudewavev1.ui.view.main.note.CurrentDateView
import com.jdosantos.gratitudewavev1.ui.view.main.note.DisplayDate
import com.jdosantos.gratitudewavev1.ui.view.main.note.DisplayEmotion
import com.jdosantos.gratitudewavev1.ui.view.main.note.DisplayTag
import com.jdosantos.gratitudewavev1.ui.view.main.note.getColors
import com.jdosantos.gratitudewavev1.ui.widget.AlertComponent
import java.util.Date

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailNoteView(
    id: String,
    color: Int?,
    detailNoteViewModel: DetailNoteViewModel,
    navController: NavController
) {

    val colors = getColors()
    val isDark = isSystemInDarkTheme()
    val note = detailNoteViewModel.note

    var selectedColor by remember { mutableStateOf(colors.getSafeColor(color)) }

    val window = (LocalView.current.context as Activity).window

    LaunchedEffect(Unit) {
        detailNoteViewModel.getNoteById(id) {noteColor->
              selectedColor = colors.getSafeColor(noteColor)
        }
    }

    LaunchedEffect(selectedColor) {
/*        window?.statusBarColor = selectedColor.toArgb()
        WindowCompat.getInsetsController(window!!, window.decorView).isAppearanceLightStatusBars =
            !isDark*/
    }


 //   var backgroundColor by remember { mutableStateOf(selectedColor) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(selectedColor, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
        horizontalAlignment = Alignment.Start
    ) {
        Header({
            navController.popBackStack()
          //  selectedColor = backgroundDefault
        }, {
            navController.navigate("UpdateNoteView/${id}/${note.color}")
          //  selectedColor = backgroundDefault
        }) {
            detailNoteViewModel.showAlert()
        }

        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {

            // HeaderInfo(note.createAt, note.updateAt, note.type)

            DisplayNote(note.note)

            DisplayTag(note.tag, 14.sp)

            DisplayEmotion(note.emotion, 14.sp)

        }


    }

    AlertDeleteNote(id, detailNoteViewModel, navController)

    DisposableEffect(Unit) {
        onDispose {
            detailNoteViewModel.clean()
        }
    }
}


@Composable
private fun HeaderInfo(createAt: Date?, updateAt: Date?, publishingOption: Int?) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        DisplayDate(modifier = Modifier, createAt, updateAt)
        Spacer(modifier = Modifier.weight(1f))
        /*DisplayPublishingType(publishingOption!!)*/
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun DisplayNote(
    note: String
) {
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
private fun Header(onExit: () -> Unit, onEdit: () -> Unit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier.padding(4.dp).height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onExit() }) {
            Icon(imageVector = Icons.Default.Close, contentDescription = ""
                ,tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        CurrentDateView(modifier = Modifier) {}
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = {
            onEdit()

        }) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = ""
                ,tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        IconButton(onClick = {
            onDelete()

        }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = ""
                ,tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
