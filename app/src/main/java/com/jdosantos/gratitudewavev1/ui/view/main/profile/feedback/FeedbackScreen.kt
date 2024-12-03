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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.MAX_LENGHT_COMMENT_FEEDBACK
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT_MAX
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT_MID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(
    navController: NavController,
    feedbackViewModel: FeedbackViewModel = hiltViewModel()
) {

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
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = ""
                        )
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
    var messageFeedback by remember { mutableStateOf("") }
    feedbackViewModel.toastMessage.observe(LocalLifecycleOwner.current, Observer { message ->
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    })

    val feedbackSuccess by feedbackViewModel.feedbackSuccess.collectAsState()
    LaunchedEffect(feedbackSuccess) {
        if (feedbackSuccess) {
            navController.popBackStack()
        }
    }
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(SPACE_DEFAULT.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {


        Text(
            text = stringResource(R.string.label_send_feedback_title_one), modifier = Modifier,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(SPACE_DEFAULT_MID.dp))
        ItemCheck(
            stringResource(R.string.label_send_feedback_item_one),
            addCheck = { text, checked -> feedbackViewModel.addCheck(text, checked) })
        ItemCheck(
            stringResource(R.string.label_send_feedback_item_two),
            addCheck = { text, checked -> feedbackViewModel.addCheck(text, checked) })
        ItemCheck(
            stringResource(R.string.label_send_feedback_item_three),
            addCheck = { text, checked -> feedbackViewModel.addCheck(text, checked) })
        ItemCheck(
            stringResource(R.string.label_send_feedback_item_four),
            addCheck = { text, checked -> feedbackViewModel.addCheck(text, checked) })
        Spacer(modifier = Modifier.height(SPACE_DEFAULT_MAX.dp))

        Text(
            text = stringResource(R.string.label_send_feedback_title_two), modifier = Modifier,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(SPACE_DEFAULT_MID.dp))
        OutlinedTextField(
            value = messageFeedback,
            onValueChange = { messageFeedback = it },
            placeholder = {
                Text(
                    text = stringResource(id = R.string.placeholder_feeback),
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.Sentences
            ),
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            maxLines = MAX_LENGHT_COMMENT_FEEDBACK,
        )

        Spacer(modifier = Modifier.height(SPACE_DEFAULT.dp))
        Spacer(modifier = Modifier.weight(1f))
        ElevatedButton(
            onClick = {
                feedbackViewModel.save(messageFeedback)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.label_send))
        }
    }
}

@Composable
private fun ItemCheck(text: String, addCheck: (String, Boolean) -> Unit) {
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        var checked by remember { mutableStateOf(false) }
        Checkbox(
            checked = checked,
            onCheckedChange = {
                checked = it
                addCheck(text, checked)
            }
        )
        Text(text = text, modifier = Modifier.padding(start = SPACE_DEFAULT_MID.dp))
    }
}