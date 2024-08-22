package com.jdosantos.gratitudewavev1.ui.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.domain.models.Note
import com.jdosantos.gratitudewavev1.ui.navigation.Screen
import com.jdosantos.gratitudewavev1.utils.getDateFromString
import com.jdosantos.gratitudewavev1.utils.getFormattedDateSimple
import com.jdosantos.gratitudewavev1.utils.toDate
import com.jdosantos.gratitudewavev1.utils.toLocalDate

@Composable
fun ListCardNotes(
    notes: List<Note>,
    listState: LazyListState,
    navController: NavController,
    onDetailNote: (Note) -> Unit
) {

    // Agrupar por fecha
    val groupedNotes = notes.groupBy { getDateFromString(it.date).toLocalDate() }
    // Ordenar los grupos por fecha
    val sortedGroups = groupedNotes.toSortedMap(compareByDescending { it })

    LazyColumn(state = listState) {

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
                key = { notes[it].idDoc },
                itemContent = { index ->
                    val note = notes[index]
                    CardNote(note, navController, showDate = false, onClick = {
                        onDetailNote(note)
                    })

                }
            )
        }

        if (notes.size == 10) {
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Solo se muestran las últimas 10 notas",
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Text(
                        text = stringResource(id = R.string.label_see_more),
                        style = MaterialTheme.typography.bodySmall.copy(
                            textDecoration = TextDecoration.Underline
                        ),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable {
                            navController.navigate(Screen.ByCalendarScreen.route)
                        }
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                }

            }
        }
    }
}