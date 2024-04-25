package com.jdosantos.gratitudewavev1.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT
import com.jdosantos.gratitudewavev1.ui.theme.skeleton
import com.jdosantos.gratitudewavev1.ui.theme.skeleton2


@Preview
@Composable
fun ComponentRectangle() {
    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(8.dp))
            .background(color = skeleton2)
            .height(120.dp)
            .fillMaxWidth()
            .shimmerLoadingAnimation() // <--- Here.
    ) {
        Column(
            modifier = Modifier.padding(SPACE_DEFAULT.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(8.dp))
                    .background(color = skeleton)
                    .height(20.dp)
                    .fillMaxWidth()
                    .shimmerLoadingAnimation() // <--- Here.
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(8.dp))
                    .background(color = skeleton)
                    .height(20.dp)
                    .fillMaxWidth()
                    .shimmerLoadingAnimation() // <--- Here.
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(8.dp))
                    .background(color = skeleton)
                    .height(20.dp)
                    .fillMaxWidth(0.3f)
                    .shimmerLoadingAnimation() // <--- Here.
            )
        }

    }
}

@Composable
fun ComponentRectangleLineLong() {
    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(8.dp))
            .background(color = Color.LightGray)
            .size(height = 30.dp, width = 200.dp)
            .shimmerLoadingAnimation() // <--- Here.
    )
}

@Composable
fun ComponentRectangleLineShort() {
    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(8.dp))
            .background(color = Color.LightGray)
            .size(height = 30.dp, width = 100.dp)
            .shimmerLoadingAnimation() // <--- Here.
    )
}