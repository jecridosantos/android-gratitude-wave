package com.jdosantos.gratitudewavev1.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT

@Composable
fun AlertComponent(
    title: String,
    message: String,
    confirmText: String,
    cancelText: String?,
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit
) {
    val scroll = rememberScrollState(0)

    AlertDialog(onDismissRequest = { onDismissClick() },
        title = { Text(text = title) },
        text = {
            Text(
                text = message,
               // textAlign = TextAlign.Justify,
                modifier = Modifier.verticalScroll(scroll)
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirmClick() }) {
                Text(text = confirmText)
            }
        },
        dismissButton = {
            if (cancelText != null) {
                TextButton(onClick = { onDismissClick() }) {
                    Text(text = cancelText)
                }
            }

        }

    )
}

@Composable
fun AlertMessageToday(
    image: Painter,
    message: String,
    confirmText: String,
    cancelText: String,
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit
) {
    val scroll = rememberScrollState(0)

    Dialog(onDismissRequest = { /*TODO*/ }) {
        Card(
            modifier = Modifier
         //       .fillMaxWidth()
              //  .height(200.dp)
                .padding(SPACE_DEFAULT.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(

                    modifier = Modifier
                        .size(80.dp)
                        .padding(8.dp),
                    painter = image,
                    contentDescription = null, // Add appropriate content description
                )

                Text(text = message)

                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = { onConfirmClick() }) {
                        Text(text = confirmText)
                    }
                    TextButton(onClick = { onDismissClick() }) {
                        Text(text = cancelText)
                    }
                }
            }
        }

    }

}