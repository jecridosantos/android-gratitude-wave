package com.jdosantos.gratitudewavev1.ui.widget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import com.jdosantos.gratitudewavev1.ui.view.main.note.DisplayDate
import com.jdosantos.gratitudewavev1.ui.view.main.note.DisplayEmotion
import com.jdosantos.gratitudewavev1.ui.view.main.note.DisplayTag
import com.jdosantos.gratitudewavev1.ui.view.main.note.getColors
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT_MID
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.VALUE_INT_EMPTY

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardNote(note: Note, navController: NavController, onClick: () -> Unit) {
    val colors = getColors()
    var expanded by remember { mutableStateOf(false) }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (note.color == null || note.color == VALUE_INT_EMPTY) colorScheme.background else colors[note.color],
        ),
        border = BorderStroke(
            width = if (note.color == null || note.color == VALUE_INT_EMPTY) 1.dp else 0.dp,
            colorScheme.outlineVariant
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = SPACE_DEFAULT_MID.dp, bottom = SPACE_DEFAULT_MID.dp)
            .combinedClickable(onLongClick = {
                expanded = true
            }, onClick = onClick),
    ) {
        Column(
            modifier = Modifier.padding(SPACE_DEFAULT.dp),
        ) {

            DisplayNote(note.note)

            Spacer(modifier = Modifier.height(8.dp))

            DisplayTag(note.noteTag, 12.sp)

            DisplayEmotion(note.emotion, 12.sp)

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Bottom
            ) {
                Spacer(modifier = Modifier.weight(1f))
                DisplayDate(modifier = Modifier, note.createAt, note.updateAt)

                //  DisplayPublishingType(note.type!!)

            }


        }


    }

    if (expanded) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            DropdownMenuItem(
                text = {
                    Text(text = "Editar")

                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "",
                        tint = colorScheme.onSurfaceVariant
                    )
                },
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

            DropdownMenuItem(
                text = { Text(text = "Compartir") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "",
                        tint = colorScheme.onSurfaceVariant
                    )
                },
                onClick = {
                    expanded = false
                    navController.navigate(Screen.SharedScreen.params(note.idDoc, note.color!!))
                }
            )
        }
    }
}


@Composable
private fun DisplayNote(note: String) {
    Text(
        text = note,
        modifier = Modifier,
        maxLines = 5,
        fontSize = 14.sp,
        overflow = TextOverflow.Ellipsis,
        //  color = colorScheme.onTertiaryContainer
    )
}

