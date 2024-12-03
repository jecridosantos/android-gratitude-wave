package com.jdosantos.gratitudewavev1.ui.view.main.home.progress

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.ui.widget.CardProgress
import com.jdosantos.gratitudewavev1.ui.widget.Title
import com.jdosantos.gratitudewavev1.ui.widget.getColors
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(
    navController: NavController,
    progressViewModel: ProgressViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        progressViewModel.initialize()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(text = stringResource(id = R.string.label_my_progress))
            })
        }
    ) { paddingValues ->
        ContentProgressView(paddingValues, progressViewModel)
    }

}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
private fun ContentProgressView(
    paddingValues: PaddingValues,
    progressViewModel: ProgressViewModel
) {

    val progressState = progressViewModel.progressState

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(SPACE_DEFAULT.dp)
            .fillMaxSize()
    ) {

        Title(text = stringResource(R.string.label_streaks), modifier = Modifier)
        Spacer(modifier = Modifier.height(16.dp))
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                CardProgress(
                    title = stringResource(R.string.label_total_notes),
                    text = "${progressState.totalNotes}",
                    color = getColors()[0]
                )
            }
            item {
                CardProgress(
                    title = stringResource(R.string.label_current_streak),
                    text = "${progressState.currentStreak} ${stringResource(id = R.string.label_days)}",
                    color = getColors()[1]
                )
            }
            item {
                CardProgress(
                    title = stringResource(R.string.label_best_streak),
                    text = "${progressState.bestStreak} ${stringResource(id = R.string.label_days)}",
                    color = getColors()[2]
                )
            }
        }
    }
}