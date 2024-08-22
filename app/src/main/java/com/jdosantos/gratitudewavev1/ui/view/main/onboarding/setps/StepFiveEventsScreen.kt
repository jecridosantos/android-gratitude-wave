package com.jdosantos.gratitudewavev1.ui.view.main.onboarding.setps

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.jdosantos.gratitudewavev1.ui.view.main.onboarding.OnboardingViewModel
import com.jdosantos.gratitudewavev1.utils.constants.Constants

@Composable
fun StepFiveEventsScreen(modifier: Modifier = Modifier, viewModel: OnboardingViewModel) {

    val preferences = viewModel.preferences
    Column(
        modifier = Modifier.padding(
            start = Constants.SPACE_DEFAULT.dp,
            end = Constants.SPACE_DEFAULT.dp,
            bottom = Constants.SPACE_DEFAULT.dp
        )
    ) {
        OutlinedTextField(
            value = preferences.about,
            onValueChange = { viewModel.onAbout(it) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.Sentences
            ),
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            maxLines = Constants.MAX_LENGHT_COMMENT_FEEDBACK,
        )
    }

}