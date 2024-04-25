package com.jdosantos.gratitudewavev1.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT

@Composable
fun EmptyMessage(painter: Int? = null, message: String, submessage: String? = null) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (painter != null) {
                Image(
                    painter = painterResource(id = painter),
                    contentDescription = null, // Add appropriate content description
                    modifier = Modifier.size(200.dp)
                )
            }

            Text(
                text = message,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center, fontWeight = FontWeight.Bold
                //, fontSize = 16.sp

            )

            if (submessage != null) {
                Text(
                    text = submessage,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center, fontWeight = FontWeight.Light, fontSize = 14.sp

                )
            }
            Spacer(modifier = Modifier.height(SPACE_DEFAULT.dp))
        }


}

@Composable
fun EmptyNotes() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.empty_notes),
                contentDescription = null, // Add appropriate content description
                modifier = Modifier.size(300.dp)
            )
            Text(
                text = "Hay algo por agradecer",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center, fontWeight = FontWeight.Light, fontSize = 16.sp

            )
            Spacer(modifier = Modifier.height(SPACE_DEFAULT.dp))
        }
    }

}

@Composable
fun EmptyMessage() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No hay notas esta fecha",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center, fontWeight = FontWeight.Light, fontSize = 16.sp
        )
    }

}

@Composable
fun EmptyNotFound() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No hay notas encontradas",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center, fontWeight = FontWeight.Light, fontSize = 16.sp
        )
    }

}