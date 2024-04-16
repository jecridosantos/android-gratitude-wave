package com.jdosantos.gratitudewavev1.ui.view.main.note

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.core.common.constants.Constants.Companion.SPACE_DEFAULT
import com.jdosantos.gratitudewavev1.core.common.util.darkColors
import com.jdosantos.gratitudewavev1.core.common.util.lightColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseColorBackground(
    selected: Int?,
    isOpen: Boolean,
    onHide: () -> Unit,
    onSelected: (Int?) -> Unit
) {
    val dark = isSystemInDarkTheme()
    val colors = if (dark) darkColors else lightColors
    if (isOpen) {
        ModalBottomSheet(onDismissRequest = { onHide() }) {

            Column(
                modifier = Modifier.padding(SPACE_DEFAULT.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.label_color),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))


                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(3),
                    //  contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(colors.size) { index ->
                        val cartItemData = colors[index]
                        CircunferenciaConIcono(index == 0, selected == index, cartItemData) {
                            onSelected(index)
                        }
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))

            }

        }
    }
}

@Composable
private fun CircunferenciaConIcono(
    isFirst: Boolean,
    isSelected: Boolean,
    circleColor: Color,
    onSelected: (Long?) -> Unit
) {
    val borderColor = colorScheme.onBackground
    Box(modifier = Modifier
        .fillMaxSize()
    //    .background(Color.Blue)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(100.dp)
                //  .background(Color.Red)
                .padding(SPACE_DEFAULT.dp)
                .clip(CircleShape)
                .clickable {
                    onSelected(
                        circleColor
                            .toArgb()
                            .toLong()
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {

                val borderWidth = 2f // Ancho del contorno

                // Dibuja el fondo de la circunferencia
                drawCircle(
                    color = circleColor,
                    radius = size.minDimension / 2
                )

                // Dibuja el contorno de la circunferencia
                drawCircle(
                    color = borderColor,
                    radius = size.minDimension / 2 - borderWidth / 2,
                    style = Stroke(width = borderWidth)
                )
            }
            // Agrega el icono en el centro de la circunferencia
            if (isFirst && !isSelected) {
                IconColor(painterResource(id = R.drawable.conjunto_vacio), borderColor)
            }

            if (isFirst && isSelected) {
                IconColor(painterResource(id = R.drawable.controlar), borderColor)
            }

            if (isSelected) {
                IconColor(painterResource(id = R.drawable.controlar), borderColor)
            }


        }
    }

}

@Composable
private fun IconColor(painter: Painter, color: Color) {
    Icon(
        painter = painter,
        contentDescription = "",
        tint = color,
        modifier = Modifier.size(36.dp)
    )
}