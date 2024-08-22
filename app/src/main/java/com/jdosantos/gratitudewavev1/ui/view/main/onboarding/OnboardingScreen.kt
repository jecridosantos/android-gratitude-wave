package com.jdosantos.gratitudewavev1.ui.view.main.onboarding

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.domain.enums.OnboardingSteps
import com.jdosantos.gratitudewavev1.domain.models.Step
import com.jdosantos.gratitudewavev1.ui.view.main.onboarding.setps.StepFiveEventsScreen
import com.jdosantos.gratitudewavev1.ui.view.main.onboarding.setps.StepFourRelationshipsScreen
import com.jdosantos.gratitudewavev1.ui.view.main.onboarding.setps.StepOnePersonalInformationScreen
import com.jdosantos.gratitudewavev1.ui.view.main.onboarding.setps.StepSixFinishScreen
import com.jdosantos.gratitudewavev1.ui.view.main.onboarding.setps.StepThreeProfessionScreen
import com.jdosantos.gratitudewavev1.ui.view.main.onboarding.setps.StepTwoInterestsScreen
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT_MID
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT_MIN
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT_TOP_APP_BAR

@Composable
fun OnboardingScreen(
    navController: NavController,
    viewModel: OnboardingViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.getUserPreferences()
    }
    val context = LocalContext.current
    val steps = viewModel.steps.collectAsState().value
    val currentStep = viewModel.currentStep.collectAsState().value
    val progress = viewModel.progress.collectAsState().value
    val stepVisibilities = viewModel.stepVisibilities.collectAsState().value

    viewModel.toastMessage.observe(LocalLifecycleOwner.current, Observer { message ->
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    })

    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetCurrentStep()
        }
    }
    Column(
        modifier =
        Modifier
            .fillMaxSize()
    ) {
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.tertiary,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())

        ) {
            for (i in stepVisibilities.indices) {
                if (stepVisibilities[i]) {
                    StepContent(navController, step = steps[i], viewModel)
                }

            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (currentStep == 0) {
                    TextButton(
                        modifier = Modifier.fillMaxWidth(0.5f),
                        onClick = {
                            viewModel.onSkip()
                            navController.popBackStack()
                        }) {
                        Text(text = stringResource(R.string.button_skip))
                    }
                } else {
                    AnimatedVisibility(visible = currentStep > 0) {
                        TextButton(
                            modifier = Modifier.fillMaxWidth(0.5f),
                            onClick = { viewModel.stepBack() }) {
                            Text(text = stringResource(R.string.button_back))
                        }
                    }
                }

                Button(modifier = Modifier.weight(1f), onClick = {
                    if (currentStep < steps.lastIndex) {
                        viewModel.stepNext()
                    } else {
                        viewModel.onSkip()
                        navController.popBackStack()
                        viewModel.saveUserPreferences()
                    }
                }) {
                    Text(
                        text = if (currentStep == steps.lastIndex) {
                            stringResource(R.string.button_finish)
                        } else {
                            stringResource(R.string.button_next)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun StepContent(navController: NavController, step: Step, viewModel: OnboardingViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box {
            Column(modifier = Modifier) {
                Row(
                    modifier = Modifier
                        .padding(SPACE_DEFAULT_MIN.dp)
                        .height(SPACE_DEFAULT_TOP_APP_BAR.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "")
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(
                            start = SPACE_DEFAULT.dp,
                            end = SPACE_DEFAULT.dp,
                            bottom = SPACE_DEFAULT.dp,
                            top = SPACE_DEFAULT_MID.dp
                        )
                ) {
                    if (step.title.isNotEmpty()) {
                        Text(text = step.title, style = MaterialTheme.typography.headlineMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    if (step.description.isNotEmpty()) {
                        Text(text = step.description, style = MaterialTheme.typography.bodyMedium)
                    }
                }


            }
        }



        when (step.step) {
            OnboardingSteps.STEP_ONE -> StepOnePersonalInformationScreen(Modifier, viewModel)
            OnboardingSteps.STEP_TWO -> StepTwoInterestsScreen(Modifier, viewModel)
            OnboardingSteps.STEP_THREE -> StepThreeProfessionScreen(Modifier, viewModel)
            OnboardingSteps.STEP_FOUR -> StepFourRelationshipsScreen(Modifier, viewModel)
            OnboardingSteps.STEP_FIVE -> StepFiveEventsScreen(Modifier, viewModel)
            OnboardingSteps.STEP_SIX -> StepSixFinishScreen(Modifier, viewModel)
        }

    }

}
