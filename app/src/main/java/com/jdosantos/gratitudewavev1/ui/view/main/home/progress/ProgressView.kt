package com.jdosantos.gratitudewavev1.ui.view.main.home.progress

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.core.common.util.challengesList
import com.jdosantos.gratitudewavev1.ui.view.main.note.getColors
import com.jdosantos.gratitudewavev1.ui.widget.CardGoal
import com.jdosantos.gratitudewavev1.ui.widget.CardProgress
import com.jdosantos.gratitudewavev1.ui.widget.Title

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressView(
    progressViewModel: ProgressViewModel,
    navController: NavController
) {
    LaunchedEffect(Unit) {
        progressViewModel.initialize()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(text = "Mi progreso")
            },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                    }
                }

            )
        }
    ) { paddingValues ->

        ContentProgressView(paddingValues, progressViewModel, navController)


    }

}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
private fun ContentProgressView(
    paddingValues: PaddingValues,
    progressViewModel: ProgressViewModel,
    navController: NavController
) {

    val progressState = progressViewModel.progressState

    val goals = progressViewModel.goals.value
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(16.dp)
            .fillMaxSize()
        //  .verticalScroll(scroll)
    ) {

        Title(text = "Rachas", modifier = Modifier)
        Spacer(modifier = Modifier.height(16.dp))
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            //  contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                CardProgress("Notas totales", "${progressState.totalNotes}", getColors()[0]) {}
            }
            item {
                CardProgress(
                    "Racha actual",
                    "${progressState.currentStreak} días",
                    getColors()[1]
                ) {}
            }
            item {
                CardProgress("Mejor racha", "${progressState.bestStreak} días", getColors()[2]) {}
            }
            /*            item {
                            CardProgress("Progreso semanal", "3/7", getColors()[3]) {}
                        }*/
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Title(text = stringResource(id = R.string.label_goals), modifier = Modifier)
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = { navController.navigate("GoalsView") }) {
                Text(text = stringResource(id = R.string.label_select))
            }
        }
        if (!goals.challenge.isNullOrEmpty()) {

            val goalsSelected = challengesList.firstOrNull { it.id == goals.challenge }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                item {
                    CardGoal(
                        painter = painterResource(id = R.drawable.calendar_goal),
                        title = goalsSelected!!.title,
                        subtitle = goalsSelected.subtite,
                        false
                    ) { }
                }

            }
        } else {
            Text(text = "Seleccionar desafío")
        }


    }
}