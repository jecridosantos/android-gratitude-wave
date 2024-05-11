package com.jdosantos.gratitudewavev1.ui.view.main.goals

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT
import com.jdosantos.gratitudewavev1.utils.challengesList
import com.jdosantos.gratitudewavev1.ui.widget.CardGoal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(navController: NavController, goalsViewModel: GoalsViewModel = hiltViewModel()) {

    LaunchedEffect(Unit) {
        goalsViewModel.getGoals()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.title_goals),
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
        ContentGoalsView(
            navController,
            goalsViewModel,
            paddingValues
        )
    }

}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
private fun ContentGoalsView(
    navController: NavController,
    goalsViewModel: GoalsViewModel,
    paddingValues: PaddingValues
) {

    val challenges = remember {
        challengesList
    }
    val goals = goalsViewModel.goals
    LazyColumn(
        Modifier
            .padding(paddingValues)
            .padding(SPACE_DEFAULT.dp)
    ) {

        items(challenges.size) {
            val challenge = challenges[it]
            val selected =
                if (!goals.challenge.isNullOrEmpty()) goals.challenge == challenge.id else false
            CardGoal(
                painter = painterResource(id = R.drawable.calendar_goal),
                title = challenge.title,
                subtitle = challenge.subtite,
                true,
                selected
                ) {
                goalsViewModel.saveChallenge(challenge.id) {}
            }
        }
    }
}