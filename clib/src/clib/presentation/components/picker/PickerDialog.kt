package clib.presentation.components.picker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import clib.presentation.components.picker.model.Picker

@Suppress("ComposeParameterOrder")
@Composable
internal fun <E> PickerDialog(
    values: List<E>,
    onDismissRequest: () -> Unit,
    @Suppress("ComposeModifierWithoutDefault") modifier: Modifier,
    textStyle: TextStyle,
    itemPadding: Int = 10,
    backgroundColor: Color,
    picker: Picker,
    content: @Composable (List<E>) -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredValues by remember(searchQuery) {
        derivedStateOf {
            if (searchQuery.isEmpty()) values
            else values.filter { it.toString().contains(searchQuery) }.ifEmpty { listOf(values.first()) }
        }
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = modifier,
            color = backgroundColor,
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {

                Spacer(modifier = Modifier.height(itemPadding.dp))

                PickerHeaderDialog(
                    title = picker.headerTitle,
                    clear = picker.clear,
                    onDismiss = { onDismissRequest() },
                )

                Spacer(modifier = Modifier.height(itemPadding.dp))

                PickerSearch(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    textStyle = textStyle,
                    hint = picker.searchHint,
                    showClearIcon = picker.showSearchClearIcon,
                )

                Spacer(modifier = Modifier.height(itemPadding.dp))

                content(filteredValues)
            }
        }
    }
}

@Composable
private fun PickerHeaderDialog(
    title: String = "Select Country",
    clear: String = "Clear",
    onDismiss: () -> Unit
) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Spacer(modifier = Modifier.width(15.dp))

        Text(
            text = title,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = { onDismiss() }) {
            Icon(Icons.Outlined.Clear, contentDescription = clear)

        }
    }
}

@Composable
private fun PickerSearch(
    value: String,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = LocalTextStyle.current,
    hint: String = "Search Country",
    clear: String = "Clear",
    showClearIcon: Boolean = true,
    requestFocus: Boolean = true,
    onFocusChanged: (FocusState) -> Unit = {}
) {
    val requester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        if (requestFocus) requester.requestFocus() else requester.freeFocus()
    }

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(requester)
            .onFocusChanged { onFocusChanged(it) },
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = textStyle,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        placeholder = {
            Text(text = hint)
        },
        leadingIcon = {
            Icon(Icons.Outlined.Search, contentDescription = hint)
        },
        trailingIcon = {
            if (showClearIcon && value.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(Icons.Outlined.Clear, contentDescription = clear)
                }
            }
        },
    )
}
