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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.core.common.util.getFormattedDateSimple
import com.jdosantos.gratitudewavev1.ui.widget.CardNote
import com.jdosantos.gratitudewavev1.ui.widget.EmptyMessage
import com.jdosantos.gratitudewavev1.ui.widget.Loader
import com.jdosantos.gratitudewavev1.ui.widget.Title

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun ByCalendarView(byCalendarViewModel: ByCalendarViewModel, navController: NavController) {
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
    val scope =
        rememberCoroutineScope()

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
            .padding(16.dp)
    ) {
        val months by byCalendarViewModel.calendar.collectAsState()

        CalendarView(months, byCalendarViewModel.selectedDate.value) {
            byCalendarViewModel.selectDate(it)
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
                            navController.navigate("DetailNoteView/${item.idDoc}/${item.color}")
                        }
                    }
                }
            } else {
             //   EmptyMessage()
                EmptyMessage(null, "Sin notas", null)
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


