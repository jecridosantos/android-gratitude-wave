package com.jdosantos.gratitudewavev1.ui.widget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jdosantos.gratitudewavev1.domain.models.Note
import com.jdosantos.gratitudewavev1.ui.navigation.Screen
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT_MID
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.VALUE_INT_EMPTY

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardNote(
    note: Note,
    navController: NavController,
    showDate: Boolean = true,
    onClick: () -> Unit
) {
    val colors = getColors()
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()

            .padding(top = SPACE_DEFAULT_MID.dp, bottom = SPACE_DEFAULT_MID.dp)
            .combinedClickable(onLongClick = {
                expanded = true
            }, onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (note.color == null || note.color == VALUE_INT_EMPTY) colorScheme.background else colors[note.color],
        ),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, if (note.color == null || note.color == VALUE_INT_EMPTY) colorScheme.surfaceVariant else colors[note.color])


    ) {
        Column(
            modifier = Modifier.padding(SPACE_DEFAULT.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = note.note,
                overflow = TextOverflow.Ellipsis,
                maxLines = 5,
                style = MaterialTheme.typography.bodyLarge,
            )

            if (note.noteTag != null || note.emotion != -1) {
                Spacer(modifier = Modifier.height(8.dp))
            }

            DisplayTag(note.noteTag)

            DisplayEmotion(note.emotion)

            Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.Bottom) {
                Spacer(modifier = Modifier.weight(1f))
                if (showDate) { DisplayDate(note.createAt, note.updateAt) }
            }

            Box(modifier = Modifier.fillMaxWidth()) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    NoteDropdownMenuItem(
                        text = "Editar",
                        icon = Icons.Default.Edit,
                        onClick = {
                            expanded = false
                            navController.navigate(
                                Screen.UpdateNoteScreen.params(
                                    note.idDoc,
                                    note.color!!
                                )
                            )
                        }
                    )

                    NoteDropdownMenuItem(
                        text = "Compartir",
                        icon = Icons.Default.Share,
                        onClick = {
                            expanded = false
                            navController.navigate(
                                Screen.SharedScreen.params(
                                    note.idDoc,
                                    note.color!!
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

