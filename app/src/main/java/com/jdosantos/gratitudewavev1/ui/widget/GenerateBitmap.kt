package com.jdosantos.gratitudewavev1.ui.widget

/*
@Composable
fun generateImageWithText(text: String, appName: String, backgroundColor: Color): Bitmap {

    val density = LocalContext.current.resources.displayMetrics.density
    val width = (200.dp.value * density).toInt()
    val height = (100.dp.value * density).toInt()


    return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
        val canvas = Canvas(this)
        canvas.drawColor(backgroundColor.toArgb())

        // Dibuja el texto en la imagen
        val paint = Paint().apply {
            color = Color.White.toArgb()
            textSize = 24.sp.value * density
            isAntiAlias = true

        }
        canvas.drawText(text, 20f * density,40f * density, paint)
        canvas.drawText(appName, 20f * density, 80f * density, paint)
    }
}
*/


/*
fun drawToBitmap(text: String, appName: String, backgroundColor: Color): ImageBitmap {
    val drawScope = CanvasDrawScope()
    val size = Size(400f, 400f) // simple example of 400px by 400px image
    val bitmap = ImageBitmap(size.width.toInt(), size.height.toInt())
    val canvas = Canvas(bitmap)

    drawScope.draw(
        density = Density(1f),
        layoutDirection = LayoutDirection.Ltr,
        canvas = canvas,
        size = size
    ) {

        drawText(TextMeasurer().)
        // Draw whatever you want here; for instance, a white background and a red line.
        drawRect(color = Color.White, topLeft = Offset.Zero, size = size)
        drawLine(
            color = Color.Red,
            start = Offset.Zero,
            end = Offset(size.width, size.height),
            strokeWidth = 5f
        )
    }
    return bitmap
}*/
