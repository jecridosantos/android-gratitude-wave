package com.jdosantos.gratitudewavev1.ui.view.main.onboarding.setps

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.ui.view.main.onboarding.OnboardingViewModel
import com.jdosantos.gratitudewavev1.utils.constants.Constants

@Composable
fun StepSixFinishScreen(modifier: Modifier = Modifier, viewModel: OnboardingViewModel) {
    Column(
        modifier = Modifier
            .padding(
                start = Constants.SPACE_DEFAULT.dp,
                end = Constants.SPACE_DEFAULT.dp,
                bottom = Constants.SPACE_DEFAULT.dp
            )
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            modifier = Modifier.padding(
                top = Constants.SPACE_DEFAULT.dp,
                start = Constants.SPACE_DEFAULT.dp,
                end = Constants.SPACE_DEFAULT.dp,
            ),
            text = "¡Gracias por completar tu información!",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            modifier = Modifier.padding(
                top = Constants.SPACE_DEFAULT.dp,
                start = Constants.SPACE_DEFAULT.dp,
                end = Constants.SPACE_DEFAULT.dp,
            ),
            text = "Recuerda que tus datos son completamente privados. Solo tú podrás ver y modificar esta información.",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.amanecer),
            contentDescription = "",
            modifier = Modifier.height(250.dp)
        )
    }
}