package com.jdosantos.gratitudewavev1.ui.widget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jdosantos.gratitudewavev1.app.model.Note
import com.jdosantos.gratitudewavev1.ui.view.main.note.DisplayDate
import com.jdosantos.gratitudewavev1.ui.view.main.note.DisplayEmotion
import com.jdosantos.gratitudewavev1.ui.view.main.note.DisplayTag
import com.jdosantos.gratitudewavev1.ui.view.main.note.getColors

@Composable
fun CardNote(note: Note, onClick: () -> Unit) {
    val colors = getColors()
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (note.color == null || note.color == -1) colorScheme.background else colors[note.color],
        ),
        /*  elevation = CardDefaults.cardElevation(
              defaultElevation = 1.dp
          ),*/
        border = BorderStroke(3.dp, colorScheme.outlineVariant),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp)
            .clickable { onClick() },
        //    shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {

            DisplayNote(note.note)

            Spacer(modifier = Modifier.height(8.dp))

            DisplayTag(note.tag, 12.sp)

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
