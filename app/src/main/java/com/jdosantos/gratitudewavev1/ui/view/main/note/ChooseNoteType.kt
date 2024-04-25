package com.jdosantos.gratitudewavev1.ui.view.main.note

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.domain.handles.PublishingOption
import com.jdosantos.gratitudewavev1.utils.publishingOptionLists
import com.jdosantos.gratitudewavev1.data.local.NoteTypeStore
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT_MID
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.VALUE_INT_EMPTY
import kotlinx.coroutines.launch

@Composable
fun ChooseNoteType(
    itemSelected: Int,
    isOpen: Boolean,
    onDismiss: (Int) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = NoteTypeStore(context)

    val indexSelected = remember { mutableIntStateOf(0) }
    val isDefault = remember { mutableStateOf(false) }

    indexSelected.intValue = itemSelected
    if (isOpen) {

        AlertDialog(onDismissRequest = {
            isDefault.value = false
        },
            title = { Text(text = stringResource(id = R.string.label_note_option_publish)) },
            text = {
                Column {

                    LazyColumn {
                        itemsIndexed(publishingOptionLists) { index, item ->
                            NoteTypeItem(indexSelected.intValue == index, item) {
                                indexSelected.intValue = index
                            }
                        }
                    }

                    CheckBoxByDefault(isDefault)

                }
            },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch {
                        if (isDefault.value) {
                            dataStore.saveDefault(indexSelected.intValue)
                        }
                    }
                    onDismiss(indexSelected.intValue)
                }) {
                    Text(text = stringResource(id = R.string.label_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss(VALUE_INT_EMPTY) }) {
                    Text(text = stringResource(id = R.string.label_cancel))
                }


            }

        )
    }

}


@Composable
private fun NoteTypeItem(selected: Boolean, item: PublishingOption, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected,
                onClick = { onClick() }),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected,
            onClick = { onClick() })
        Spacer(modifier = Modifier.width(4.dp))


        Row {
            Text(
                stringResource(item.title),
                modifier = Modifier.weight(1f)
            )
            Icon(painter = painterResource(id = item.icon), contentDescription = "")
            Spacer(modifier = Modifier.width(8.dp))

        }

    }
    Divider()
}

@Composable
private fun CheckBoxByDefault(isDefault: MutableState<Boolean>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = SPACE_DEFAULT.dp)
            .selectable(
                selected = isDefault.value,
                onClick = { isDefault.value = !isDefault.value }),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Checkbox(
            checked = isDefault.value,
            onCheckedChange = { isDefault.value = !isDefault.value },
            modifier = Modifier.padding(end = SPACE_DEFAULT_MID.dp)
        )

        Text(
            text = stringResource(id = R.string.publishing_option_label_default),
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start
        )
    }
}
