package com.jdosantos.gratitudewavev1.ui.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jdosantos.gratitudewavev1.core.common.constants.Constants

@Composable
fun ConfigItem(onClick: () -> Unit, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .height(Constants.HEIGHT_ITEMS_CONFIG.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier.align(Alignment.Center).padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
        ) {
            content()
        }

    }
}