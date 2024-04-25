package com.jdosantos.gratitudewavev1.ui.view.main.note

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.domain.models.NoteTag
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT
import com.jdosantos.gratitudewavev1.ui.widget.ItemSelectedOptions


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseNoteTag(
    tagSelected: String?,
    isOpen: Boolean,
    noteTags: List<NoteTag>,
    onHide: () -> Unit,
    onSelected: (NoteTag?) -> Unit) {
    if (isOpen) {
    ModalBottomSheet(onDismissRequest = { onHide() }) {

        Column(
            modifier = Modifier.padding(SPACE_DEFAULT.dp)
        ) {
            TitleTagModal()
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                itemsIndexed(noteTags) { index, tag ->

                    ItemSelectedOptions(tagSelected == tag.id, tag.esTag) {
                        onSelected(tag)
                    }

                }
            }
            Spacer(modifier = Modifier.width(16.dp))

        }
    }
    }
}

@Composable
fun TitleTagModal() {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(id = R.string.title_selec_tag),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
}
