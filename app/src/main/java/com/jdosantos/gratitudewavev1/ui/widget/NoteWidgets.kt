package com.jdosantos.gratitudewavev1.ui.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.domain.enums.Emotion
import com.jdosantos.gratitudewavev1.domain.models.Note
import com.jdosantos.gratitudewavev1.domain.models.NoteTag
import com.jdosantos.gratitudewavev1.utils.combineDateAndTime
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.MAX_LENGHT_COMMENT_FEEDBACK
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT_MID
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.VALUE_INT_EMPTY
import com.jdosantos.gratitudewavev1.utils.convertMillisToDate
import com.jdosantos.gratitudewavev1.utils.darkColors
import com.jdosantos.gratitudewavev1.utils.emotionLists
import com.jdosantos.gratitudewavev1.utils.getDateFromString
import com.jdosantos.gratitudewavev1.utils.getFormattedDate
import com.jdosantos.gratitudewavev1.utils.lightColors
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentDateView(date: String? = null, createAt: Date? = null,clickeable: Boolean = false, onClick: (Date) -> Unit) {
    var dateToShow = Date()
    if (!date.isNullOrEmpty() && createAt != null) {
        val dateFormated = getDateFromString(date)
        dateToShow = combineDateAndTime(dateFormated, createAt)
    }
    //val dateState = rememberDatePickerState()


    val dateState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis <= System.currentTimeMillis()
            }
        }
    )

    val selectedDate = dateState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: dateToShow
    var showDialog by remember { mutableStateOf(false) }
    var changeView by remember { mutableStateOf(false) }

    if (clickeable) {
        Row(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(8.dp))
                .clickable {
                    showDialog = true
                }
        ) {
            Text(
                text = getFormattedDate(if (changeView) selectedDate else dateToShow),
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 4.dp)
            )
            Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "")
        }


        if (showDialog) {
            DatePickerDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    Button(
                        onClick = {
                            showDialog = false
                            changeView = true
                            onClick(selectedDate)
                        }
                    ) {
                        Text(text = stringResource(id = R.string.label_confirm))
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDialog = false }
                    ) {
                        Text(text = stringResource(id = R.string.label_cancel))
                    }
                }
            ) {
                DatePicker(
                    state = dateState,
                    showModeToggle = true
                )
            }
        }
    } else {


            Text(
                text = getFormattedDate(dateToShow),
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 4.dp)
            )




    }


}

@Composable
fun ChipEmotionChoose(emotion: Emotion, onClick: () -> Unit, onClean: () -> Unit) {
    ElevatedAssistChip(
        onClick = { onClick() },
        label = { Text(stringResource(id = emotion.message)) },
        leadingIcon = { Text(stringResource(id = emotion.icon)) },
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
fun DisplayEmotion(emotionIndex: Int?, style: TextStyle = MaterialTheme.typography.bodySmall) {
    if (emotionIndex != VALUE_INT_EMPTY) {
        val noteEmotionConfig = emotionLists[emotionIndex!!]
        Text(
            text = "${stringResource(id = noteEmotionConfig.message)} ${stringResource(id = noteEmotionConfig.icon)}",
           // color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = style
        )
        Spacer(modifier = Modifier.height(4.dp))
    }

}

@Composable
fun DisplayTag(noteTag: NoteTag?, style: TextStyle = MaterialTheme.typography.bodySmall) {
    if (noteTag != null) {
        Text(
            text = noteTag.esTag,
            overflow = TextOverflow.Ellipsis,
           // color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
            style = style
        )
        Spacer(modifier = Modifier.height(4.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NoteInputFiled(
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
//        colors = TextFieldDefaults.outlinedTextFieldColors(
//            focusedBorderColor = Color.Transparent, unfocusedBorderColor = Color.Transparent
//        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Default,
            capitalization = KeyboardCapitalization.Sentences
        ),
        maxLines = MAX_LENGHT_COMMENT_FEEDBACK,
        shape = RoundedCornerShape(15.dp),
        modifier = modifier
            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
            .fillMaxWidth()
            .height(200.dp),
        textStyle = TextStyle.Default.copy(fontSize = 24.sp)
    )
}

@Composable
fun NoteInput(
    note: Note,
    isLoading: Boolean = false,
    onNoteChange: (String) -> Unit,
    onDispose: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    Column {
        NoteInputFiled(
            note.note, modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
        ) {
            onNoteChange(it)
        }

        Loader(isLoading)

    }

    DisposableEffect(Unit) {
        focusRequester.requestFocus()
        onDispose {
            onDispose()
        }
    }
}

@Composable
fun DisplayDate(date: Date?, updatedAt: Date?) {

    val formattedDate = updatedAt?.let { getFormattedDate(it).lowercase(Locale.ROOT) }
        ?: date?.let { getFormattedDate(it) }

    if (formattedDate != null) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            updatedAt?.let {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_update_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = formattedDate,
                color = MaterialTheme.colorScheme.tertiary,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}


@Composable
fun CardItems(icon: Int, text: String, color: Color, onClick: () -> Unit) {
    Box(modifier = Modifier
        .padding(bottom = SPACE_DEFAULT_MID.dp)
        .fillMaxWidth()
        .clickable { onClick() }) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = color,
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(SPACE_DEFAULT.dp)
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = "",
                    Modifier.size(50.
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = text,
                    //fontWeight = FontWeight.ExtraBold,
                    maxLines = 1,
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis
                )
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
fun FloatingOptions(
    modifier: Modifier,
    showGenerator: Boolean? = false,
    onClickTag: () -> Unit,
    onClickEmotion: () -> Unit,
    onClickColor: () -> Unit,
    onClickGenerate: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),

    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
        ) {

            IconFloatingOption(painterResource(id = R.drawable.chinche)) { onClickTag() }

            IconFloatingOption(painterResource(id = R.drawable.haz_de_sonrisa)) { onClickEmotion() }

            IconFloatingOption(painterResource(id = R.drawable.paleta)) { onClickColor() }
            AnimatedVisibility(visible = showGenerator!!) {

                    IconFloatingOption2(painter = painterResource(id = R.drawable.baseline_auto_awesome_24)) { onClickGenerate() }

            }


        }
    }
}

@Composable
private fun IconFloatingOption(painter: Painter, onClick: () -> Unit) {
    IconButton(onClick = { onClick() }, modifier = Modifier.padding(start = 1.dp, end = 1.dp)) {
        Icon(
            painter,
            contentDescription = "",
            Modifier.size(20.dp)
        )

    }
}

@Composable
private fun IconFloatingOption2(painter: Painter, onClick: () -> Unit) {
    TextButton(onClick = { onClick() }, modifier = Modifier.padding(start = 1.dp, end = 1.dp)) {
        Icon(
            painter,
            contentDescription = "",
            Modifier.padding(end = 8.dp)
        )
        Text(text = stringResource(R.string.label_improve_my_words))

    }
}

@Composable
fun NoticeWithoutNotesToday(modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_auto_awesome_24),
                contentDescription = "",
                modifier = Modifier.padding(8.dp)
            )
            Column {
                Text(
                    text = "Aún no has agregado tu nota del día.",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

    }

    Spacer(modifier = Modifier.height(16.dp))
}