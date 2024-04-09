@file:Suppress("PreviewAnnotationInFunctionWithParameters")

package com.jdosantos.gratitudewavev1.ui.view.main.note.writenote

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.app.model.Note
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

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun WriteNoteView(writeNoteViewModel: WriteNoteViewModel, navController: NavController) {

    val context = LocalContext.current

    val isDark = isSystemInDarkTheme()

    val colors = getColors()

    val backgroundDefault = colorScheme.background

    var selectedColor by remember { mutableStateOf(backgroundDefault) }

    val window = (LocalView.current.context as Activity).window

    val note = writeNoteViewModel.note

    LaunchedEffect(Unit) {
        writeNoteViewModel.init(context)
    }

    LaunchedEffect(selectedColor) {
/*        window?.statusBarColor = selectedColor.toArgb()
        WindowCompat.getInsetsController(window!!, window.decorView).isAppearanceLightStatusBars =
            !isDark*/
    }

    fun cleanView() {
    //    writeNoteViewModel.clean()
        navController.popBackStack()
    }

    Scaffold(
 /*       topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.tab_label_home),
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor =
                )
            )
        }*/

    ) { paddingValues ->

        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()

                .background(selectedColor, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),

        ) {

            Column() {
                Header({
                    writeNoteViewModel.saveNewNote({ cleanView() }) {

                    }
                }) {
                    cleanView()
                }

                NoteInput(note, {
                    writeNoteViewModel.onNote(it)
                }) {
                    writeNoteViewModel.clean()
                }

                Tags(note, writeNoteViewModel)
            }

            FloatingOptions(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp), writeNoteViewModel
            )

        }
    }

    ChooseNoteEmotion(note.emotion!!, writeNoteViewModel.showDialogEmotion,{
        writeNoteViewModel.hideDialogEmotion()
    }) {
        writeNoteViewModel.onEmotion(it)
    }

    ChooseColorBackground(note.color, writeNoteViewModel.showDialogColor, {
        writeNoteViewModel.hideDialogColor()
    }) {

        if (it != null) {
            selectedColor = if (it == -1) {
                backgroundDefault
            } else {
                colors[it]
            }
            writeNoteViewModel.onColor(it)
/*            window?.statusBarColor = selectedColor.toArgb()
            WindowCompat.getInsetsController(window!!, window.decorView).isAppearanceLightStatusBars =
                !isDark*/
        }


       // writeNoteViewModel.hideDialogColor()
    }

    ChooseNoteTag(
        note.tag?.id,
        writeNoteViewModel.showDialogTag,
        writeNoteViewModel.tags.value,
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
  /*          window?.statusBarColor = backgroundDefault.toArgb()
            WindowCompat.getInsetsController(window!!, window.decorView).isAppearanceLightStatusBars =
                !isDark*/

           // selectedColor = backgroundDefault
        }
    }

}


@Composable
private fun Header(onSave: () -> Unit, onCleanView: () -> Unit) {
    Row(
        modifier = Modifier.padding(4.dp).height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onCleanView() }) {
            Icon(imageVector = Icons.Default.Close, contentDescription = ""
                ,tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(modifier = Modifier.width(4.dp))
        CurrentDateView(modifier = Modifier) {}
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = {
            onSave()

        }) {
            Icon(imageVector = Icons.Default.Check, contentDescription = ""
                ,tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun Tags(note: Note, writeNoteViewModel: WriteNoteViewModel) {
    Column(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
    ) {

        if (note.tag != null) {
            Row {
                ChipTagChoose(note.tag, {
                    writeNoteViewModel.showDialogTag()
                }) {
                    writeNoteViewModel.onTag(null)
                }
            }
        }

        if (note.emotion != -1) {
            Row {
                ChipEmotionChoose(noteEmotionConfigLists[note.emotion!!], {
                    writeNoteViewModel.showDialogEmotion()
                }) {
                    writeNoteViewModel.onEmotion(-1)
                }
            }
        }

    }
}

@Composable
private fun FloatingOptions(modifier: Modifier, writeNoteViewModel: WriteNoteViewModel) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(50.dp),
    ) {
        Row(
            modifier = Modifier.padding(4.dp)
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            IconFloatingOption(painterResource(id = R.drawable.chinche)) {  writeNoteViewModel.showDialogTag()}
            Spacer(modifier = Modifier.width(8.dp))
            IconFloatingOption(painterResource(id = R.drawable.haz_de_sonrisa)) { writeNoteViewModel.showDialogEmotion() }
            Spacer(modifier = Modifier.width(8.dp))
            IconFloatingOption(painterResource(id = R.drawable.paleta)) { writeNoteViewModel.showDialogColor() }

            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}