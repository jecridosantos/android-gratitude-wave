package com.jdosantos.gratitudewavev1.ui.widget

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun Title(text: String, modifier: Modifier?) {
    Text(text, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, modifier = modifier!!)
}