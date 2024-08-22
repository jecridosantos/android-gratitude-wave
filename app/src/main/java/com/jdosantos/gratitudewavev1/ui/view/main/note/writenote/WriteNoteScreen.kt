@file:Suppress("PreviewAnnotationInFunctionWithParameters")

package com.jdosantos.gratitudewavev1.ui.view.main.note.writenote

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.domain.models.Note
import com.jdosantos.gratitudewavev1.ui.view.main.note.ChooseColorBackground
import com.jdosantos.gratitudewavev1.ui.view.main.note.ChooseNoteEmotion
import com.jdosantos.gratitudewavev1.ui.view.main.note.ChooseNoteTag
import com.jdosantos.gratitudewavev1.ui.widget.ChipEmotionChoose
import com.jdosantos.gratitudewavev1.ui.widget.ChipTagChoose
import com.jdosantos.gratitudewavev1.ui.widget.CurrentDateView
import com.jdosantos.gratitudewavev1.ui.widget.FloatingOptions
import com.jdosantos.gratitudewavev1.ui.widget.NoteInput
import com.jdosantos.gratitudewavev1.ui.widget.getColors
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT_TOP_APP_BAR
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.VALUE_INT_EMPTY
import com.jdosantos.gratitudewavev1.utils.emotionLists

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun WriteNoteScreen(
    navController: NavController,
    writeNoteViewModel: WriteNoteViewModel
) {

    val context = LocalContext.current
    val isLoading by writeNoteViewModel.isLoading.collectAsState()
    val colors = getColors()

    val backgroundDefault = colorScheme.background

    var selectedColor by remember { mutableStateOf(backgroundDefault) }

    val note = writeNoteViewModel.note

    fun cleanView() = navController.popBackStack()

    LaunchedEffect(Unit) {
        writeNoteViewModel.init(context)
    }

    writeNoteViewModel.toastMessage.observe(LocalLifecycleOwner.current, Observer { message ->
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    })

    Scaffold() { paddingValues ->

        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(

                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            selectedColor,
                        )
                    )
                ),

            ) {

            Column() {
                Header(
                    writeNoteViewModel,
                    onSave = {
                        writeNoteViewModel.saveNewNote()
                        cleanView()
                    }, onCleanView = {
                        cleanView()
                    }
                )

                NoteInput(
                    note = note,
                    isLoading = isLoading,
                    onNoteChange = {
                        writeNoteViewModel.onNote(it)
                    },
                    onDispose = {
                        writeNoteViewModel.clean()
                    })


                Tags(note, writeNoteViewModel)
            }

            FloatingOptions(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(SPACE_DEFAULT.dp),
                showGenerator = note.note.length > 10,
                onClickTag = { writeNoteViewModel.showDialogTag() },
                onClickEmotion = { writeNoteViewModel.showDialogEmotion() },
                onClickColor = { writeNoteViewModel.showDialogColor() },
                onClickGenerate = { writeNoteViewModel.generateMessage() }
            )

        }
    }

    ChooseNoteEmotion(note.emotion!!, writeNoteViewModel.showDialogEmotion, {
        writeNoteViewModel.hideDialogEmotion()
    }) {
        writeNoteViewModel.onEmotion(it)
    }

    ChooseColorBackground(note.color, writeNoteViewModel.showDialogColor, {
        writeNoteViewModel.hideDialogColor()
    }) {

        if (it != null) {
            selectedColor = if (it == VALUE_INT_EMPTY) {
                backgroundDefault
            } else {
                colors[it]
            }
            writeNoteViewModel.onColor(it)
        }

    }

    ChooseNoteTag(
        note.noteTag?.id,
        writeNoteViewModel.showDialogTag,
        writeNoteViewModel.noteTags.value,
        {
            writeNoteViewModel.hideDialogTag()
        }
    ) {
        writeNoteViewModel.onTag(it)
    }


    if (writeNoteViewModel.showErrorForm) {
        Toast.makeText(
            context,
            stringResource(id = R.string.login_note_form_error_empty),
            Toast.LENGTH_LONG
        ).show()
        writeNoteViewModel.showErrorForm = false
    }

    DisposableEffect(Unit) {
        onDispose {
        }
    }

}


@Composable
private fun Header(
    writeNoteViewModel: WriteNoteViewModel,
    onSave: () -> Unit,
    onCleanView: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(4.dp)
            .height(SPACE_DEFAULT_TOP_APP_BAR.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onCleanView() }) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "",
                tint = colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        CurrentDateView(clickeable = true) {
            writeNoteViewModel.onDate(it)
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = {
            onSave()

        }) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "",
                tint = colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun Tags(note: Note, writeNoteViewModel: WriteNoteViewModel) {
    Column(
        modifier = Modifier.padding(start = SPACE_DEFAULT.dp, end = SPACE_DEFAULT.dp)
    ) {

        if (note.noteTag != null) {
            Row {
                ChipTagChoose(note.noteTag, {
                    writeNoteViewModel.showDialogTag()
                }) {
                    writeNoteViewModel.onTag(null)
                }
            }
        }

        if (note.emotion != VALUE_INT_EMPTY) {
            Row {
                ChipEmotionChoose(emotionLists[note.emotion!!], {
                    writeNoteViewModel.showDialogEmotion()
                }) {
                    writeNoteViewModel.onEmotion(VALUE_INT_EMPTY)
                }
            }
        }

    }
}

