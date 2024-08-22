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
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT_MID

@Composable
fun StepFourRelationshipsScreen(modifier: Modifier = Modifier, viewModel: OnboardingViewModel) {
    val preferences = viewModel.preferences
    var selectedRelationships by remember { mutableStateOf(preferences.selectedRelationships) }
    val persons = viewModel.relationshipsList.collectAsState().value

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(
                start = SPACE_DEFAULT.dp,
                end = SPACE_DEFAULT.dp,
                bottom = SPACE_DEFAULT.dp
            )
        ) {
            persons.forEach { person ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = selectedRelationships.contains(person),
                        onCheckedChange = { isChecked ->
                            val updatedSelection = if (isChecked) {
                                selectedRelationships + person
                            } else {
                                selectedRelationships.filterNot { it == person }
                            }
                            selectedRelationships = updatedSelection
                            viewModel.onRelationshipsList(selectedRelationships)
                        }
                    )
                    Text(text = person, modifier = Modifier.padding(start = SPACE_DEFAULT_MID.dp))
                }
            }
        }

        if (selectedRelationships.contains(persons.last())) {

            InputRound(
                label = stringResource(R.string.label_other_people),
                value = preferences.relationship,
                placeholder = stringResource(R.string.label_other_people_placeholder),
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ) {
                viewModel.onRelationship(it)
            }
        }
    }
}