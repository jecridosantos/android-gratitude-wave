package com.jdosantos.gratitudewavev1.ui.widget

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.jdosantos.gratitudewavev1.ui.navigation.Screen

@Composable
fun NoteDropdownMenuItem(text: String, icon: ImageVector, onClick: () -> Unit) {
    DropdownMenuItem(
        text = {
            Text(text = text)
        },
        trailingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        onClick = {
            onClick()
        }
    )
}