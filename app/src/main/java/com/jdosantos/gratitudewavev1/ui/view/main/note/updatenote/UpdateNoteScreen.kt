package com.jdosantos.gratitudewavev1.ui.view.main.note.updatenote

import android.annotation.SuppressLint
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT_MIN
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT_TOP_APP_BAR
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.VALUE_INT_EMPTY
import com.jdosantos.gratitudewavev1.utils.emotionLists
import com.jdosantos.gratitudewavev1.utils.getSafeColor

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun UpdateNoteScreen(
    id: String,
    color: Int,
    navController: NavController,
    updateNoteViewModel: UpdateNoteViewModel
) {
    val isLoading by updateNoteViewModel.isLoading.collectAsState()
    val context = LocalContext.current

    val colors = getColors()

    val backgroundDefault = MaterialTheme.colorScheme.background

    var selectedColor by remember { mutableStateOf(colors.getSafeColor(color.toInt())) }

    val note = updateNoteViewModel.note

    LaunchedEffect(Unit) {
        updateNoteViewModel.getNoteById(id)
    }


    fun cleanView() {
        navController.popBackStack()
    }

    updateNoteViewModel.toastMessage.observe(LocalLifecycleOwner.current, Observer { message ->
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    })
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
            ),
    ) {
        Column() {
            Header(
                note = note,
                updateNoteViewModel,
                {
//                updateNoteViewModel.updateNote { success ->
//                    if (success) cleanView()
                    //}
                    updateNoteViewModel.updateNote()
                    cleanView()
                }) {
                cleanView()
            }
            NoteInput(
                note = note,
                isLoading = isLoading,
                onNoteChange = {
                    updateNoteViewModel.onNote(it)
                },
                onDispose = {
                    updateNoteViewModel.clean()
                })

            if (note.note.length > 10) {
                OutlinedButton(
                    modifier = Modifier
                        .padding(SPACE_DEFAULT.dp),
                    onClick = {
                        updateNoteViewModel.generateMessage()
                    })
                {
                    Text(text = stringResource(R.string.label_improve_my_words))
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_auto_awesome_24),
                        contentDescription = ""
                    )
                }
            }

            Tags(note, updateNoteViewModel)
        }


        FloatingOptions(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(SPACE_DEFAULT.dp),
            onClickTag = { updateNoteViewModel.showDialogTag() },
            onClickEmotion = { updateNoteViewModel.showDialogEmotion() },
            onClickColor = { updateNoteViewModel.showDialogColor() },
            onClickGenerate = { updateNoteViewModel.generateMessage() }
        )

    }

    ChooseNoteEmotion(note.emotion!!, updateNoteViewModel.showDialogEmotion, {
        updateNoteViewModel.hideDialogEmotion()
    }) {
        updateNoteViewModel.onEmotion(it)
    }

    ChooseNoteTag(
        note.noteTag?.id,
        updateNoteViewModel.showDialogTag,
        updateNoteViewModel.tags.value,
        {
            updateNoteViewModel.hideDialogTag()
        }) {
        updateNoteViewModel.onTag(it)
    }

    ChooseColorBackground(note.color, updateNoteViewModel.showDialogColor, {
        updateNoteViewModel.hideDialogColor()
    }) {

        if (it != null) {
            selectedColor = if (it == VALUE_INT_EMPTY) {
                backgroundDefault
            } else {
                colors[it]
            }
            updateNoteViewModel.onColor(it)
        }
    }
    if (updateNoteViewModel.showErrorForm) {
        Toast.makeText(
            context,
            stringResource(id = R.string.login_note_form_error_empty),
            Toast.LENGTH_LONG
        ).show()

        updateNoteViewModel.showErrorForm = false
    }


}


@Composable
private fun Header(
    note: Note,
    updateNoteViewModel: UpdateNoteViewModel,
    onSave: () -> Unit,
    onCleanView: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(SPACE_DEFAULT_MIN.dp)
            .height(SPACE_DEFAULT_TOP_APP_BAR.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onCleanView() }) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        CurrentDateView(date = note.date, createAt = note.createAt, clickeable = true) {
            updateNoteViewModel.onDate(it)
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = {
            onSave()

        }) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


@Composable
private fun Tags(note: Note, updateNoteViewModel: UpdateNoteViewModel) {
    Column(
        modifier = Modifier.padding(start = SPACE_DEFAULT.dp, end = SPACE_DEFAULT.dp)
    ) {

        if (note.noteTag != null) {
            Row {
                ChipTagChoose(note.noteTag, {
                    updateNoteViewModel.showDialogTag()
                }) {
                    updateNoteViewModel.onTag(null)
                }
            }
        }

        if (note.emotion != VALUE_INT_EMPTY) {
            Row {
                ChipEmotionChoose(emotionLists[note.emotion!!], {
                    updateNoteViewModel.showDialogEmotion()
                }) {
                    updateNoteViewModel.onEmotion(VALUE_INT_EMPTY)
                }
            }
        }

    }
}
