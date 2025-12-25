package ui.auth.emal.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clib.data.type.orErrorColor
import clib.data.type.primitives.string.stringResource
import clib.presentation.components.textfield.TextField
import clib.presentation.navigation.NavigationAction
import compose_app.generated.resources.Res
import compose_app.generated.resources.clear
import compose_app.generated.resources.email
import compose_app.generated.resources.username
import data.type.primitives.string.asStringResource
import klib.data.type.primitives.string.LINE_SEPARATOR
import klib.data.validator.Validator
import ui.auth.emal.presentation.viewmodel.EmailAction
import ui.auth.emal.presentation.viewmodel.EmailState
import ui.navigation.presentation.Email

@Composable
public fun EmailScreen(
    modifier: Modifier = Modifier,
    route: Email = Email(),
    validator: Validator = Validator.email(),
    state: EmailState = EmailState(),
    onAction: (EmailAction) -> Unit = {},
    onNavigationActions: (Array<NavigationAction>) -> Unit = {},
): Unit = Box(
    modifier = modifier,
    contentAlignment = Alignment.Center,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Default.Email,
            contentDescription = stringResource(Res.string.email),
            modifier = Modifier.size(128.dp),
        )

        Text(
            text = stringResource(Res.string.email),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
            overflow = TextOverflow.Clip,
            maxLines = 1,
        )

        val focusRequester = FocusRequester()
        var isValid by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        TextField(
            modifier = Modifier.fillMaxWidth(0.8f).focusRequester(focusRequester),
            value = state.email,
            onValueChange = { value -> onAction(EmailAction.SetEmail(value)) },
            textStyle = MaterialTheme.typography.bodyMedium,
            label = { isError ->
                Text(
                    text = stringResource(Res.string.email),
                    color = LocalContentColor.current.orErrorColor(isError),
                    overflow = TextOverflow.Clip,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            placeholder = { isError ->
                Text(
                    text = stringResource(Res.string.username),
                    color = LocalContentColor.current.orErrorColor(isError),
                    overflow = TextOverflow.Clip,
                    maxLines = 1,
                )
            },
            leadingIcon = { isError ->
                Icon(
                    Icons.Default.Email,
                    stringResource(Res.string.email),
                    Modifier.padding(horizontal = 4.dp),
                    LocalContentColor.current.orErrorColor(isError),
                )
            },
            clearIcon = { action ->
                Icon(
                    Icons.Default.Close,
                    stringResource(Res.string.clear),
                    Modifier.padding(horizontal = 4.dp).clickable(onClick = action),
                    MaterialTheme.colorScheme.error,
                )
            },
            isError = !isValid,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (isValid) onAction(EmailAction.Confirm)
                },
            ),
            singleLine = true,
            outlined = true,
            validator = validator,
            onValidation = { value ->
                isValid = value.isEmpty()
                value.map { it.asStringResource() }.joinToString(String.LINE_SEPARATOR)
            },
        )
    }
}

@Preview
@Composable
private fun PreviewEmailScreen(): Unit = EmailScreen()
