package com.jdosantos.gratitudewavev1.ui.view.main.note.sharednote

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.Intent.createChooser
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Picture
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.domain.models.Note
import com.jdosantos.gratitudewavev1.ui.widget.getColors
import com.jdosantos.gratitudewavev1.utils.constants.Constants
import com.jdosantos.gratitudewavev1.utils.getSafeColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import kotlin.coroutines.resume


//@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedNoteScreen(
    navController: NavController,
    sharedNoteViewModel: SharedNoteViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val picture = remember { Picture() }

    val colors = getColors()
    val note = sharedNoteViewModel.note
    var isChecked by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf(colors.getSafeColor(sharedNoteViewModel.color.toInt())) }
    var permission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted -> permission = isGranted })

    LaunchedEffect(Unit) {
        sharedNoteViewModel.getCurrentUser()
        sharedNoteViewModel.getNoteById(sharedNoteViewModel.id) { noteColor ->
            selectedColor = colors.getSafeColor(noteColor)
        }
        permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
    fun shareBitmapFromComposable() {

        coroutineScope.launch(Dispatchers.IO) {
            val bitmap = createBitmapFromPicture(picture)
            val uri = bitmap.saveToDisk(context)
            shareBitmap(context, uri, note.note)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.label_shared_note),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                    }

                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },

        ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(

                    //RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            selectedColor,

                            )
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .drawWithCache {
                        val density = context.resources.displayMetrics.density
                        val width = (400.dp.value * density).toInt()
                        val height = (500.dp.value * density).toInt()
                        onDrawWithContent {
                            val pictureCanvas =
                                androidx.compose.ui.graphics.Canvas(
                                    picture.beginRecording(
                                        width,
                                        height
                                    )
                                )
                            draw(this, this.layoutDirection, pictureCanvas, this.size) {
                                this@onDrawWithContent.drawContent()
                            }
                            picture.endRecording()

                            drawIntoCanvas { canvas -> canvas.nativeCanvas.drawPicture(picture) }
                        }
                    }
            ) {
                ScreenContentToCapture(sharedNoteViewModel.user.name, note, selectedColor, isChecked)
            }
            if (!(note.color == null || note.color == Constants.VALUE_INT_EMPTY)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = Constants.SPACE_DEFAULT_MID.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = {
                            isChecked = it
                        }
                    )
                    Text(
                        text = "Compartir con fondo blanco.",
                        modifier = Modifier.padding(start = Constants.SPACE_DEFAULT_MID.dp)
                    )
                }
            }

            ElevatedButton(onClick = {
                shareBitmapFromComposable()
            }) {
                Text(text = "Compartir")
                Icon(Icons.Default.Share, "share")
            }



        }


    }
}

@Composable
private fun ScreenContentToCapture(
    name: String,
    note: Note,
    color: Color,
    isChecked: Boolean = false
) {
    val colors = getColors()
    Card(

        modifier = Modifier
            .size(400.dp, 500.dp)
            .padding(16.dp)
            .fillMaxWidth()

            .padding(top = Constants.SPACE_DEFAULT_MID.dp)
         ,
        colors = CardDefaults.cardColors(
            containerColor = if ((note.color == null || note.color == Constants.VALUE_INT_EMPTY) || isChecked) MaterialTheme.colorScheme.background else colors[note.color!!],
        ),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, if ((note.color == null || note.color == Constants.VALUE_INT_EMPTY) || isChecked) MaterialTheme.colorScheme.surfaceVariant else colors[note.color!!])
    ) {
        Column(

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(start = 32.dp, end = 32.dp)
                .fillMaxSize()
        ) {
            Image(
                painterResource(id = R.drawable.yuspa_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                note.note,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 5,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Por $name",
                textAlign = TextAlign.End,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

}

private fun createBitmapFromPicture(picture: Picture): Bitmap {
    val bitmap = Bitmap.createBitmap(
        picture.width,
        picture.height,
        Bitmap.Config.ARGB_8888
    )

    val canvas = android.graphics.Canvas(bitmap)
    canvas.drawColor(android.graphics.Color.WHITE)
    canvas.drawPicture(picture)
    return bitmap
}

private suspend fun Bitmap.saveToDisk(context: Context): Uri {
    val file = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
        "screenshot-${System.currentTimeMillis()}.png"
    )

    file.writeBitmap(this, Bitmap.CompressFormat.PNG, 100)

    return scanFilePath(context, file.path) ?: throw Exception("File could not be saved")
}


private suspend fun scanFilePath(context: Context, filePath: String): Uri? {
    return suspendCancellableCoroutine { continuation ->
        MediaScannerConnection.scanFile(
            context,
            arrayOf(filePath),
            arrayOf("image/png")
        ) { _, scannedUri ->
            if (scannedUri == null) {
                continuation.cancel(Exception("File $filePath could not be scanned"))
            } else {
                continuation.resume(scannedUri)
            }
        }
    }
}

private fun File.writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int) {
    outputStream().use { out ->
        bitmap.compress(format, quality, out)
        out.flush()
    }
}

private fun shareBitmap(context: Context, uri: Uri, message: String) {

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/png"
        putExtra(Intent.EXTRA_STREAM, uri)
        putExtra(Intent.EXTRA_TEXT, message)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    startActivity(context, createChooser(intent, "Share your image"), null)
}