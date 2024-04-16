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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.app.model.Note
import com.jdosantos.gratitudewavev1.core.common.constants.Constants.Companion.SPACE_DEFAULT
import com.jdosantos.gratitudewavev1.core.common.constants.Constants.Companion.SPACE_DEFAULT_MIN
import com.jdosantos.gratitudewavev1.core.common.constants.Constants.Companion.VALUE_INT_EMPTY
import com.jdosantos.gratitudewavev1.core.common.util.getSafeColor
import com.jdosantos.gratitudewavev1.core.common.util.noteEmotionConfigLists
import com.jdosantos.gratitudewavev1.ui.view.main.note.ChipEmotionChoose
import com.jdosantos.gratitudewavev1.ui.view.main.note.ChipTagChoose
import com.jdosantos.gratitudewavev1.ui.view.main.note.ChooseColorBackground
import com.jdosantos.gratitudewavev1.ui.view.main.note.ChooseNoteEmotion
import com.jdosantos.gratitudewavev1.ui.view.main.note.ChooseNoteTag
import com.jdosantos.gratitudewavev1.ui.view.main.note.CurrentDateView
import com.jdosantos.gratitudewavev1.ui.view.main.note.IconFloatingOption
import com.jdosantos.gratitudewavev1.ui.view.main.note.NoteInput
import com.jdosantos.gratitudewavev1.ui.view.main.note.getColors

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateNoteView(
    id: String,
    color: Int?,
    updateNoteViewModel: UpdateNoteViewModel,
    navController: NavController
) {

    val context = LocalContext.current

    val colors = getColors()

    val backgroundDefault = MaterialTheme.colorScheme.background

    var selectedColor by remember { mutableStateOf(colors.getSafeColor(color)) }

    val note = updateNoteViewModel.note

    LaunchedEffect(Unit) {
        updateNoteViewModel.getNoteById(id)
    }


    fun cleanView() {
        navController.popBackStack()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(selectedColor, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
    ) {
        Column() {
            Header({
                updateNoteViewModel.updateNote({ cleanView() }) {

                }
            }) {
                cleanView()
            }
            NoteInput(note, {
                updateNoteViewModel.onNote(it)
            }) {
                updateNoteViewModel.clean()
            }
            Tags(note, updateNoteViewModel)
        }

        FloatingOptions(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(SPACE_DEFAULT.dp), updateNoteViewModel
        )

    }

    ChooseNoteEmotion(note.emotion!!, updateNoteViewModel.showDialogEmotion, {
        updateNoteViewModel.hideDialogEmotion()
    }) {
        updateNoteViewModel.onEmotion(it)
    }

    ChooseNoteTag(note.tag?.id, updateNoteViewModel.showDialogTag, updateNoteViewModel.tags.value, {
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
private fun Header(onSave: () -> Unit, onCleanView: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(SPACE_DEFAULT_MIN.dp)
            .height(56.dp),
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
        CurrentDateView(modifier = Modifier) {}
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

        if (note.tag != null) {
            Row {
                ChipTagChoose(note.tag, {
                    updateNoteViewModel.showDialogTag()
                }) {
                    updateNoteViewModel.onTag(null)
                }
            }
        }

        if (note.emotion != VALUE_INT_EMPTY) {
            Row {
                ChipEmotionChoose(noteEmotionConfigLists[note.emotion!!], {
                    updateNoteViewModel.showDialogEmotion()
                }) {
                    updateNoteViewModel.onEmotion(VALUE_INT_EMPTY)
                }
            }
        }

    }
}

@Composable
private fun FloatingOptions(modifier: Modifier, viewModel: UpdateNoteViewModel) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(50.dp),
    ) {
        Row(
            modifier = Modifier.padding(SPACE_DEFAULT_MIN.dp)
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            IconFloatingOption(painterResource(id = R.drawable.chinche)) { viewModel.showDialogTag() }
            Spacer(modifier = Modifier.width(8.dp))
            IconFloatingOption(painterResource(id = R.drawable.haz_de_sonrisa)) { viewModel.showDialogEmotion() }
            Spacer(modifier = Modifier.width(8.dp))
            IconFloatingOption(painterResource(id = R.drawable.paleta)) { viewModel.showDialogColor() }

            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}