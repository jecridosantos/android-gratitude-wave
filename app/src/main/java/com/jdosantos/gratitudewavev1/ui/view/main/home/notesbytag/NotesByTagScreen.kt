package com.jdosantos.gratitudewavev1.ui.view.main.home.notesbytag

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.ui.navigation.Screen
import com.jdosantos.gratitudewavev1.ui.widget.CardNote
import com.jdosantos.gratitudewavev1.ui.widget.EmptyMessage
import com.jdosantos.gratitudewavev1.ui.widget.Loader
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT
import com.jdosantos.gratitudewavev1.utils.getFormattedDateSimple
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesByTagScreen(
    navController: NavController,
    notesByTagViewModel: NotesByTagViewModel = hiltViewModel(),
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = notesByTagViewModel.tagName,
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
    fun LocalDate.toDate(): Date {
        return Date.from(this.atStartOfDay(ZoneId.systemDefault()).toInstant())
    }

    fun Date.toLocalDate(): LocalDate {
        return this.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }
    // Agrupar por fecha
    val groupedNotes = notes.groupBy {
        it.createAt!!
            .toLocalDate()
    }
    // Ordenar los grupos por fecha
    val sortedGroups = groupedNotes.toSortedMap(compareByDescending { it })
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(start = SPACE_DEFAULT.dp, end = SPACE_DEFAULT.dp)
    ) {
        Loader(isLoading)
        if (!isLoading) {
            if (notes.isNotEmpty()) {
                LazyColumn {

                    sortedGroups.forEach { (date, notes) ->
                        item {
                            Text(
                                text = getFormattedDateSimple(date.toDate()),
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                        items(
                            count = notes.size,
                            key = {
                                notes[it].idDoc
                            },
                            itemContent = { index ->
                                val item = notes[index]

                                CardNote(item, navController, showDate = false, onClick = {
                                    navController.navigate(
                                        Screen.DetailNoteScreen.params(
                                            item.idDoc,
                                            item.color!!
                                        )
                                    )
                                })

                            }

                        )


                    }
                }
            } else {
                EmptyMessage(null, stringResource(R.string.label_no_notes), null)
            }
        }
    }
}