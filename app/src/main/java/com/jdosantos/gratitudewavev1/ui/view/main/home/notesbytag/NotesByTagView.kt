package com.jdosantos.gratitudewavev1.ui.view.main.home.notesbytag

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.core.common.constants.Constants.Companion.SPACE_DEFAULT
import com.jdosantos.gratitudewavev1.ui.widget.CardNote
import com.jdosantos.gratitudewavev1.ui.widget.EmptyMessage
import com.jdosantos.gratitudewavev1.ui.widget.Loader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesByTagView(
    tagId: String,
    tagName: String,
    notesByTagViewModel: NotesByTagViewModel,
    navController: NavController
) {

    val window = (LocalView.current.context as Activity).window
    val colorBackground = MaterialTheme.colorScheme.background
    val dark = isSystemInDarkTheme()
    LaunchedEffect(colorBackground) {
        window?.statusBarColor = colorBackground.toArgb()
        WindowCompat.getInsetsController(window!!, window.decorView).isAppearanceLightStatusBars =
            !dark
    }

    LaunchedEffect(Unit) {
        notesByTagViewModel.fetchNotes(tagId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = tagName,
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
        }

    ) { paddingValues ->
        ContentNotesByTagView(paddingValues = paddingValues, notesByTagViewModel, navController)
    }
}

@Composable
fun ContentNotesByTagView(
    paddingValues: PaddingValues,
    notesByTagViewModel: NotesByTagViewModel,
    navController: NavController
) {
    val isLoading by notesByTagViewModel.isLoading.collectAsState()
    val notes by notesByTagViewModel.notes.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(start = SPACE_DEFAULT.dp, end = SPACE_DEFAULT.dp)
    ) {

        if (isLoading) {
            Loader()
        } else {
            if (notes.isNotEmpty()) {
                LazyColumn {
                    items(notes) { item ->
                        CardNote(item) {
                            navController.navigate("DetailNoteView/${item.idDoc}/${item.color}")
                        }
                    }
                }
            } else {
                EmptyMessage(null, stringResource(R.string.label_no_notes), null)
            }
        }
    }
}