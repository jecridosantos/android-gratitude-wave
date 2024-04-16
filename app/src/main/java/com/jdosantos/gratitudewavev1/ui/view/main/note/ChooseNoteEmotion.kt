package com.jdosantos.gratitudewavev1.ui.view.main.note

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.core.common.confignote.NoteEmotionConfig
import com.jdosantos.gratitudewavev1.core.common.constants.Constants.Companion.SPACE_DEFAULT
import com.jdosantos.gratitudewavev1.core.common.util.noteEmotionConfigLists
import com.jdosantos.gratitudewavev1.ui.widget.ItemSelectedOptions


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseNoteEmotion(
    indexSelected: Int,
    isOpen: Boolean,
    onHide: () -> Unit,
    onSelected: (Int?) -> Unit,
) {
    if (isOpen) {
        ModalBottomSheet(onDismissRequest = { onHide() }) {

            Column(
                modifier = Modifier.padding(SPACE_DEFAULT.dp)
            ) {
                TitleModal()
                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn {
                    itemsIndexed(noteEmotionConfigLists) { index, emotion ->

                      /*  EmotionItem(index == indexSelected, emotion) {
                            onSelected(index)
                        }*/

                        val title = "${stringResource(id = emotion.icon)} ${stringResource(id = emotion.title)}"

                        ItemSelectedOptions(index == indexSelected, title) {
                            onSelected(index)
                        }
                    }

                }
                Spacer(modifier = Modifier.width(16.dp))

            }

        }
    }
}

@Composable
fun TitleModal() {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(id = R.string.title_optiones_emoticons),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
}

@Composable
fun EmotionItem(selected: Boolean, item: NoteEmotionConfig, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            //  .height(48.dp)
            .selectable(
                selected = selected, onClick = onClick
            )
            .padding(SPACE_DEFAULT.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(id = item.icon), fontSize = 18.sp)
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = stringResource(id = item.title),
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp
        )
        if (selected) {
            Spacer(modifier = Modifier.weight(1f))
            Icon(imageVector = Icons.Default.Check, contentDescription = "")
        }

    }
}
