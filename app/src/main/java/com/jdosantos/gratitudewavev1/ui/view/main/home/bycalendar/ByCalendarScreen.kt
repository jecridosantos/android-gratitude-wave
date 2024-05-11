package com.jdosantos.gratitudewavev1.ui.view.main.home.bycalendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.ui.navigation.Screen
import com.jdosantos.gratitudewavev1.ui.widget.CardNote
import com.jdosantos.gratitudewavev1.ui.widget.EmptyMessage
import com.jdosantos.gratitudewavev1.ui.widget.Loader
import com.jdosantos.gratitudewavev1.ui.widget.Title
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT
import com.jdosantos.gratitudewavev1.utils.getFormattedDateSimple

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun ByCalendarScreen(
    navController: NavController,
    byCalendarViewModel: ByCalendarViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.tab_label_home),
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
        ContentByCalendarView(
            byCalendarViewModel,
            navController,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun ContentByCalendarView(
    byCalendarViewModel: ByCalendarViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {

    val isLoading by byCalendarViewModel.isLoading.collectAsState()
    val data by byCalendarViewModel.notesData.collectAsState()

    LaunchedEffect(Unit) {
        if (!byCalendarViewModel.isNavigateToDetail.value) {
            byCalendarViewModel.searchTodayNotes()
        }
        byCalendarViewModel.navigateToDetail(false)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(SPACE_DEFAULT.dp)
    ) {
        val months by byCalendarViewModel.calendar.collectAsState()

        if (months.isNotEmpty()) {
            CalendarView(months, byCalendarViewModel.selectedDate.value) {
                byCalendarViewModel.selectDate(it)
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        Title(
            text = getFormattedDateSimple(byCalendarViewModel.selectedDate.value),
            modifier = Modifier
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (isLoading) {
            Loader()
        } else {
            if (data.isNotEmpty()) {
                LazyColumn {
                    items(data) { item ->
                        CardNote(item) {
                            byCalendarViewModel.navigateToDetail(true)
                            navController.navigate(
                                Screen.DetailNoteScreen.params(
                                    item.idDoc,
                                    item.color!!
                                )
                            )
                        }
                    }
                }
            } else {
                //   EmptyMessage()
                EmptyMessage(null, stringResource(id = R.string.label_no_notes), null)
            }
        }
    }


    DisposableEffect(Unit) {
        onDispose {
            if (!byCalendarViewModel.isNavigateToDetail.value) {
                byCalendarViewModel.clean()
            }
        }
    }

}


