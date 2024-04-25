package com.jdosantos.gratitudewavev1.ui.view.main.profile.feedback

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.MAX_LENGHT_COMMENT_FEEDBACK
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT_MID
import com.jdosantos.gratitudewavev1.ui.widget.Title

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackView(navController: NavController, feedbackViewModel: FeedbackViewModel) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.label_send_feedback),
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
        ContentFeedbackView(paddingValues, feedbackViewModel, navController)
    }

}

@Composable
private fun ContentFeedbackView(
    paddingValues: PaddingValues,
    feedbackViewModel: FeedbackViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    var message by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(SPACE_DEFAULT.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        Title(
            text = stringResource(R.string.label_send_feedback_title_one),
            modifier = Modifier.padding(bottom = SPACE_DEFAULT.dp)
        )


        ItemCheck(stringResource(R.string.label_send_feedback_item_one), feedbackViewModel)
        ItemCheck(stringResource(R.string.label_send_feedback_item_two), feedbackViewModel)
        ItemCheck(stringResource(R.string.label_send_feedback_item_three), feedbackViewModel)
        ItemCheck(stringResource(R.string.label_send_feedback_item_four), feedbackViewModel)
        Spacer(modifier = Modifier.height(32.dp))
        Title(
            text = stringResource(R.string.label_send_feedback_title_two),
            modifier = Modifier.padding(bottom = SPACE_DEFAULT.dp)
        )
        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            placeholder = {
                Text(
                    text = stringResource(id = R.string.placeholder_feeback),
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            maxLines = MAX_LENGHT_COMMENT_FEEDBACK,
        )

        Spacer(modifier = Modifier.height(16.dp))
        Spacer(modifier = Modifier.weight(1f))
        ElevatedButton(
            onClick = {
                feedbackViewModel.save(
                    message,
                    callback = { success ->
                        Toast.makeText(
                            context,
                            context.getString(if (success) R.string.label_success_feedback else R.string.label_error_feedback),
                            Toast.LENGTH_LONG
                        ).show()
                        if (success) navController.popBackStack()

                    })
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.label_send))
        }
    }
}

@Composable
private fun ItemCheck(text: String, viewModel: FeedbackViewModel) {
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        var checked by remember { mutableStateOf(false) }
        Checkbox(
            checked = checked,
            onCheckedChange = {
                checked = it
                viewModel.addCheck(text, checked)
            }
        )
        Text(text = text, modifier = Modifier.padding(start = SPACE_DEFAULT_MID.dp))
    }
}