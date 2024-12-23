package com.jdosantos.gratitudewavev1.ui.view.main.home.tags

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.ui.navigation.Screen
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT_MID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchByTagsScreen(
    navController: NavController,
    searchByTagsViewModel: SearchByTagsViewModel = hiltViewModel()
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.label_search_by_tag),
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
        }

    ) { paddingValues ->
        ContentSearchByTagsView(paddingValues = paddingValues, searchByTagsViewModel, navController)
    }

}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ContentSearchByTagsView(
    paddingValues: PaddingValues,
    searchByTagsViewModel: SearchByTagsViewModel,
    navController: NavController
) {

    val tags = searchByTagsViewModel.tags.value

    Column(
        modifier = Modifier
            .padding(paddingValues)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            contentPadding = PaddingValues(SPACE_DEFAULT_MID.dp)
        ) {
            items(tags) {
                Card(modifier = Modifier
                    .padding(4.dp)
                    .clickable {
                        navController.navigate(Screen.NotesByTagScreen.params(it.id, it.esTag))
                    }) {
                    Text(
                        text = it.esTag,
                        modifier = Modifier.padding(SPACE_DEFAULT.dp)
                    )
                }
            }
        }
    }
}