package com.jdosantos.gratitudewavev1.ui.widget

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.jdosantos.gratitudewavev1.R

@Composable
fun Title(text: String, modifier: Modifier?) {

    Text(text, style = MaterialTheme.typography.bodyMedium, modifier = modifier!!)
}

@Composable
fun TextItem(text: String, modifier: Modifier?) {
    Text(text, modifier = modifier!!)
}
