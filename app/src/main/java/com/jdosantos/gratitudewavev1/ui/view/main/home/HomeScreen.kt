package com.jdosantos.gratitudewavev1.ui.view.main.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.ui.navigation.Screen
import com.jdosantos.gratitudewavev1.ui.widget.EmptyMessage
import com.jdosantos.gratitudewavev1.ui.widget.ImageAvatar
import com.jdosantos.gratitudewavev1.ui.widget.ListCardNotes
import com.jdosantos.gratitudewavev1.ui.widget.Loader
import com.jdosantos.gratitudewavev1.ui.widget.NoticeWithoutNotesToday
import com.jdosantos.gratitudewavev1.ui.widget.isScrollingUp
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.MAX_LENGHT_TITLE_TOP_BAR
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val navigateToNewNote: () -> Unit = {
        navController.navigate(Screen.WriteNoteScreen.route)
    }
    val backPressedOnce = remember { mutableStateOf(false) }
    val context = LocalContext.current

    BackHandler(enabled = true) {
        handleBackPressed(context, backPressedOnce)
    }

    LaunchedEffect(Unit) {
        homeViewModel.fetchNotes()
    }

    val listState = rememberLazyListState()
    val userName by homeViewModel.userName.collectAsState()
    val userAvatar by homeViewModel.userAvatar.collectAsState()
    val showOnboarding by homeViewModel.showOnboarding.collectAsState()
    if (showOnboarding) {
        navController.navigate(Screen.OnboardingScreen.route)
        homeViewModel.setShowOnboarding(false)
    }

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = userName,
                        maxLines = MAX_LENGHT_TITLE_TOP_BAR,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                ),
                navigationIcon = {
                    IconButton(onClick = {

                    }) {
                        ImageAvatar(
                            painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(userAvatar)
                                    .build()
                            )
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.SearchNoteScreen.route) }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "")
                    }
                }
            )
        },

        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = "Agradecer") },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_pencil),
                        modifier = Modifier.size(24.dp),
                        contentDescription = ""
                    )
                },
                expanded = listState.isScrollingUp(),
                onClick = { navigateToNewNote() }
            )
        }


    ) {
        ContentHomeView(it, homeViewModel, navController, listState)
    }


}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ContentHomeView(
    paddingValues: PaddingValues,
    homeViewModel: HomeViewModel,
    navController: NavController,
    listState: LazyListState
) {
    val isLoading by homeViewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(start = SPACE_DEFAULT.dp, end = SPACE_DEFAULT.dp)
    ) {

        val notes by homeViewModel.notesData.collectAsState()

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.label_home_today), modifier = Modifier,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.weight(1f))
            if (notes.isNotEmpty()) {
                TextButton(onClick = { navController.navigate(Screen.ByCalendarScreen.route) }) {
                    Text(
                        text = stringResource(id = R.string.label_see_more),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

        }

        Loader(isLoading)
        if (!isLoading) {

            if (!homeViewModel.thereAreNotesToday.value && homeViewModel.isFetch.value) {
                NoticeWithoutNotesToday()
            }
            if (notes.isNotEmpty()) {
                ListCardNotes(
                    notes = notes,
                    listState = listState,
                    navController = navController,
                ) { navController.navigate(Screen.DetailNoteScreen.params(it.idDoc, it.color!!)) }

            } else {
                if (homeViewModel.isFetch.value) {
                    EmptyMessage(
                        R.drawable.transistor_empty_envelope_with_fly_inside,
                        stringResource(R.string.label_no_notes),
                        stringResource(R.string.label_no_notes_today_message)
                    )
                }

            }
        }
    }

}

private fun handleBackPressed(
    context: Context,
    backPressedOnceState: MutableState<Boolean>
) {
    if (backPressedOnceState.value) {
        Toast.makeText(context, R.string.label_exit_to_app, Toast.LENGTH_SHORT).show()
        (context as? Activity)?.finish()
    } else {
        backPressedOnceState.value = true
    }
}


