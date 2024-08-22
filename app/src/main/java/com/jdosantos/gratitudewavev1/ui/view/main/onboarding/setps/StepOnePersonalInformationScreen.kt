package com.jdosantos.gratitudewavev1.ui.view.main.onboarding.setps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.ui.view.main.onboarding.OnboardingViewModel
import com.jdosantos.gratitudewavev1.ui.widget.InputRound

@Composable
fun StepOnePersonalInformationScreen(
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel
) {

    val preferences = viewModel.preferences
    LaunchedEffect(Unit) {
        viewModel.getCurrentUser()
    }

    InputRound(
        label = stringResource(id = R.string.label_name),
        value = preferences.name,
        placeholder = stringResource(id = R.string.label_name_placeholder),
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next
    ) {
        viewModel.onName(it)
    }

    InputRound(
        label = stringResource(id = R.string.label_age),
        value = preferences.age,
        placeholder = stringResource(id = R.string.label_age_placeholder),
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Next
    ) {
        viewModel.onAge(it)
    }

    InputRound(
        label = stringResource(id = R.string.label_country),
        value = preferences.country,
        placeholder = stringResource(id = R.string.label_country_placeholder),
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Done
    ) {
        viewModel.onCountry(it.trimStart())
    }
}