package com.jdosantos.gratitudewavev1.ui.view.main.note

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.utils.emotionLists
import com.jdosantos.gratitudewavev1.utils.darkColors
import com.jdosantos.gratitudewavev1.utils.getFormattedDate
import com.jdosantos.gratitudewavev1.utils.lightColors
import com.jdosantos.gratitudewavev1.domain.models.Note
import com.jdosantos.gratitudewavev1.domain.models.NoteTag
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT_MID
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT_MIN
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.VALUE_INT_EMPTY
import com.jdosantos.gratitudewavev1.domain.enums.Emotion
import com.jdosantos.gratitudewavev1.ui.widget.CardNote
import java.util.Date
import java.util.Locale

@Composable
fun CurrentDateView(modifier: Modifier, onClick: () -> Unit) {
    Text(
        text = getFormattedDate(Date()),
        fontSize = 12.sp,
        modifier = modifier
    )
}

@Composable
fun ChipEmotionChoose(
    emotion: Emotion,
    onClick: () -> Unit,
    onClean: () -> Unit
) {
    ElevatedAssistChip(
        onClick = { onClick() },
        label = { Text(stringResource(id = emotion.message)) },
        leadingIcon = {
            Text(stringResource(id = emotion.icon))
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "",
                modifier = Modifier.clickable {
                    onClean()
                })
        }
    )
}

@Composable
fun ChipTagChoose(noteTag: NoteTag, onClick: () -> Unit?, onClean: () -> Unit) {
    ElevatedAssistChip(
        onClick = { onClick() },
        label = { Text(noteTag.esTag) },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "",
                modifier = Modifier.clickable {
                    onClean()
                })
        }
    )
}


@Composable
fun DisplayEmotion(emotionIndex: Int?, size: TextUnit) {
    if (emotionIndex != VALUE_INT_EMPTY) {
        val noteEmotionConfig = emotionLists[emotionIndex!!]
        Text(
            text = "${stringResource(id = noteEmotionConfig.message)} ${stringResource(id = noteEmotionConfig.icon)}",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium, fontSize = size,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(4.dp))
    }

}

@Composable
fun DisplayTag(noteTag: NoteTag?, size: TextUnit) {
    if (noteTag != null) {
        Text(
            text = noteTag.esTag,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium,
            fontSize = size
        )
        Spacer(modifier = Modifier.height(4.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteInputFiled(
    note: String,
    modifier: Modifier,
    onValueChange: (value: String) -> Unit
) {
    OutlinedTextField(
        value = note,
        onValueChange = { onValueChange(it) },
        placeholder = {
            Text(
                text = stringResource(id = R.string.placeholder_write_note),
                fontSize = 24.sp
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Transparent, unfocusedBorderColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Default
        ),
        modifier = modifier,
        //  maxLines = 7,
        textStyle = TextStyle.Default.copy(fontSize = 24.sp)
    )
}

@Composable
fun ItemOptionsNote(
    icon2: Int?,
    title: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(start = SPACE_DEFAULT.dp, end = SPACE_DEFAULT.dp, top = SPACE_DEFAULT_MIN.dp, bottom = SPACE_DEFAULT_MIN.dp)
                .height(40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon2 != null) {
                Icon(painter = painterResource(icon2), contentDescription = "")
                Spacer(modifier = Modifier.width(24.dp))
            }
            Text(
                text = title, modifier = Modifier.weight(1f), fontWeight = FontWeight.Normal
            )
        }
        Divider(modifier = Modifier.align(Alignment.TopCenter))
    }
}

@Composable
fun NoteInput(note: Note, onNoteChange: (String) -> Unit, onDispose: () -> Unit) {
    val focusRequester = remember { FocusRequester() }

    Column() {
        NoteInputFiled(
            note.note, modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
        ) {
            onNoteChange(it)
        }

    }

    DisposableEffect(Unit) {
        focusRequester.requestFocus()
        onDispose {
            onDispose()

        }
    }
}

@Composable
fun DisplayDate(modifier: Modifier, date: Date?, updatedAt: Date?) {
    if (updatedAt != null) {
        Row {

            Icon(
                painter = painterResource(id = R.drawable.baseline_update_24),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = " ${getFormattedDate(updatedAt).lowercase(Locale.ROOT)}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Light
            )
        }

    } else {
        if (date != null) {
            Text(
                text = getFormattedDate(date),
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 12.sp, fontWeight = FontWeight.Light
            )
        }
    }

}

@Composable
fun ShowListGirdNotes(notes: List<Note>, onClick: (Note) -> Unit) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(SPACE_DEFAULT_MID.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(notes) { item ->

            CardNote(item) {
                onClick(item)
            }
        }
    }
}

@Composable
fun ShowListNotes(notes: List<Note>, listState: LazyListState, onClick: (Note) -> Unit) {
    LazyColumn(
        state = listState
    ) {

        items(
            count = notes.size,
            key = {
                notes[it].idDoc
            },
            itemContent = { index ->
                val cartItemData = notes[index]
                CardNote(cartItemData) {
                    onClick(cartItemData)
                }
            }
        )
    }
}

@Composable
fun CardItems(icon: Int, text: String, color: Color, onClick: () -> Unit) {
    Box(modifier = Modifier.padding(bottom = SPACE_DEFAULT_MID.dp)
        .fillMaxWidth().clickable { onClick() }) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = color,
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            ),
            modifier = Modifier .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(SPACE_DEFAULT.dp)
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = "",
                    Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = text,
                    //fontWeight = FontWeight.ExtraBold,
                    maxLines = 1,
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
fun getColors(): List<Color> {
    val isDark = isSystemInDarkTheme()
    return if (isDark) darkColors else lightColors
}

@Composable
fun IconFloatingOption(painter: Painter, onClick: () -> Unit) {
    IconButton(onClick = { onClick() }) {
        Icon(
            painter,
            contentDescription = "",
            Modifier.size(24.dp)
        )

    }
}