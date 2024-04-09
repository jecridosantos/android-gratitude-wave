package com.jdosantos.gratitudewavev1.ui.view.main.home.search

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.app.model.Tag
import com.jdosantos.gratitudewavev1.ui.widget.Title

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchNoteView(
    searchNoteViewModel: SearchNoteViewModel, navController: NavController
) {
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val notes by searchNoteViewModel.notesData.collectAsState()

    val window = (LocalView.current.context as Activity).window
    val colorBackground = MaterialTheme.colorScheme.background
    val dark = isSystemInDarkTheme()
    LaunchedEffect(colorBackground) {
/*        window?.statusBarColor = colorBackground.toArgb()
        WindowCompat.getInsetsController(window!!, window.decorView).isAppearanceLightStatusBars =
            !dark*/
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBar(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = if (active) 0.dp else 8.dp),
            query = query,
            onQueryChange = { query = it },
            onSearch = { active = false },
            active = active,

            onActiveChange = { active = it },
            placeholder = { Text(text = stringResource(id = R.string.title_label_search_my_notes)) },
            leadingIcon = {
                if (!active) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                    }

                } else {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "")
                }

            },
            trailingIcon = {

                if (active) {
                    IconButton(onClick = { active = false }) {
                        Icon(imageVector = Icons.Default.Close,
                            contentDescription = "")
                    }

                } else {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "")
                }


            }) {
            if (query.isNotBlank()) {
                val filterGames = notes.filter { it.note.contains(query, ignoreCase = true) }
                filterGames.forEach {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {

                        HighlightedText(query, it.note) {
                            navController.navigate("DetailNoteView/${it.idDoc}/${it.color}")
                        }
                    }
                }
            }
        }

        if (searchNoteViewModel.tags.value.isNotEmpty()) {
            Tags(searchNoteViewModel.tags.value, {
                navController.navigate("NotesByTagView/${it.id}/${it.esTag}")
            }) {
                navController.navigate("SearchByTagsView")
            }
        }

        /* if (notes.isNotEmpty()) {

             Column(
                 modifier = Modifier
                     .fillMaxSize()
                     .padding(start = 16.dp, end = 16.dp)
             )
             {
                 Title("Mi última nota", modifier = Modifier.padding(start = 0.dp, bottom = 16.dp, top = 16.dp))
                 LazyColumn {
                     items(notes.take(1)) { item ->
                         CardNote(item, showDate = true) {
                             navController.navigate("DetailNoteView/${item.idDoc}")
                         }
                     }
                 }
             }

         }*/
    }
}

@Composable
fun HighlightedText(searchText: String, content: String, onClick: () -> Unit) {
    val annotatedText = buildAnnotatedString {
        val startIndex = content.indexOf(searchText, ignoreCase = true)
        if (startIndex == -1) {
            append(content)
        } else {
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
            ) {
                if (startIndex > 10) {
                    append("..." + content.substring(startIndex - 5, startIndex))
                } else {
                    append(content.substring(0, startIndex))
                }
            }

            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            ) {
                append(content.substring(startIndex, startIndex + searchText.length))
            }

            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
            ) {
                append(content.substring(startIndex + searchText.length))
            }
        }
    }

    ClickableText(
        text = annotatedText,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(bottom = 10.dp, start = 10.dp),
        onClick = {
            onClick()
        })
}

@Composable
fun Tags(tags: List<Tag>, onSearch: (tag: Tag) -> Unit, onClick: () -> Unit) {
    Column(modifier = Modifier.padding(4.dp)) {

        Title(
            "Buscar por etiqueta",
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp, top = 16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(tags.take(8)) {
                Card(modifier = Modifier
                    .padding(4.dp)
                    .clickable {
                        onSearch(it)

                    }
                ) {
                    Text(
                        text = it.esTag,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        ElevatedButton(
            onClick = { onClick() },
            modifier = Modifier
                .padding(start = 16.dp, bottom = 16.dp, top = 8.dp, end = 16.dp)
                .fillMaxWidth(),

            ) {
            Text(text = "Ver todas")

        }
    }

}