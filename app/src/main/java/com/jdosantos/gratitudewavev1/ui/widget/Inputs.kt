package com.jdosantos.gratitudewavev1.ui.widget

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InputRound(
    label: String,
    value: String,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Default,
    maxLines: Int = 1,
    readOnly: Boolean = false,
    onChange: (String) -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }
    OutlinedTextField(
        label = { Text(text = label) },
        value = value,
        onValueChange = { text ->
            onChange(text)
        },
        placeholder = { Text(text = placeholder) },
        visualTransformation = if (keyboardType == KeyboardType.Password && !passwordVisible) {
            PasswordVisualTransformation()
        } else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction,
            capitalization = if (keyboardType == KeyboardType.Text) KeyboardCapitalization.Sentences else KeyboardCapitalization.None
        ),
        shape = RoundedCornerShape(15.dp),
        maxLines = maxLines,
        readOnly = readOnly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = SPACE_DEFAULT.dp, end = SPACE_DEFAULT.dp, bottom = SPACE_DEFAULT.dp),
        trailingIcon = {
            if (keyboardType == KeyboardType.Password) {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = if (passwordVisible) painterResource(id = R.drawable.baseline_visibility_24) else painterResource(
                            id = R.drawable.baseline_visibility_off_24
                        ),
                        contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    )
                }
            }
        }
    )
}

