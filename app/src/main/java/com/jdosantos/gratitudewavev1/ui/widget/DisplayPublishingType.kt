package com.jdosantos.gratitudewavev1.ui.widget

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jdosantos.gratitudewavev1.core.common.constants.Constants.Companion.VALUE_INT_EMPTY
import com.jdosantos.gratitudewavev1.core.common.util.noteTypeConfigLists

@Composable
fun DisplayPublishingType(index: Int) {
    if (index != VALUE_INT_EMPTY) {
        Icon(
            painter = painterResource(id = noteTypeConfigLists[index].icon),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.size(14.dp)
        )
    }
}
