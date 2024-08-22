package com.jdosantos.gratitudewavev1.ui.view.main.onboarding.setps

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.ui.view.main.onboarding.OnboardingViewModel
import com.jdosantos.gratitudewavev1.ui.widget.InputRound
import com.jdosantos.gratitudewavev1.utils.constants.Constants
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT_MID


@Composable
fun StepTwoInterestsScreen(modifier: Modifier = Modifier, viewModel: OnboardingViewModel) {
    val preferences = viewModel.preferences

    val interests = viewModel.interestsList.collectAsState().value
    var selectedInterests by remember { mutableStateOf(preferences.selectedInterests) }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(
                start = Constants.SPACE_DEFAULT.dp,
                end = Constants.SPACE_DEFAULT.dp,
                bottom = Constants.SPACE_DEFAULT.dp
            )
        ) {
            interests.forEach { interest ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = selectedInterests.contains(interest),
                        onCheckedChange = { isChecked ->
                            val updatedSelection = if (isChecked) {
                                selectedInterests + interest
                            } else {
                                selectedInterests.filterNot { it == interest }
                            }
                            selectedInterests = updatedSelection
                            viewModel.onInterestsList(selectedInterests)
                        }
                    )
                    Text(text = interest, modifier = Modifier.padding(start = SPACE_DEFAULT_MID.dp))
                }
            }
        }

        if (selectedInterests.contains(interests.last())) {

            InputRound(
                label = stringResource(R.string.label_other_interest),
                value = preferences.interests,
                placeholder = stringResource(R.string.label_other_interest_placeholder),
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ) {
                viewModel.onInterests(it)
            }
        }
    }
}