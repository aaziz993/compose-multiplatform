package clib.presentation.components.textfield.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import clib.data.type.orErrorColor
import clib.data.type.state.timePickerStateToTime
import clib.generated.resources.Res
import clib.generated.resources.case_sensitive
import clib.generated.resources.regex
import clib.generated.resources.whole_word
import clib.presentation.components.picker.TimePickerDialog
import clib.presentation.components.textfield.TextField
import clib.presentation.components.textfield.model.TextField
import clib.presentation.components.textfield.search.model.SearchFieldCompare
import clib.presentation.components.textfield.search.model.SearchFieldState
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.EqualsSolid
import compose.icons.lineawesomeicons.GreaterThanEqualSolid
import compose.icons.lineawesomeicons.GreaterThanSolid
import compose.icons.lineawesomeicons.LessThanEqualSolid
import compose.icons.lineawesomeicons.LessThanSolid
import compose.icons.lineawesomeicons.MinusSolid
import compose.icons.lineawesomeicons.NotEqualSolid
import klib.data.type.primitives.time.now
import klib.data.type.primitives.time.toEpochMilliseconds
import klib.data.validator.Validator
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import org.jetbrains.compose.resources.painterResource

@Composable
public fun SearchField(
    state: SearchFieldState,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    showValue: Boolean = true,
    onShowValueChange: ((Boolean) -> Unit)? = null,
    enabled: Boolean = true,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable ((isError: Boolean) -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable ((isError: Boolean) -> Unit)? = null,
    trailingIcon: @Composable ((isError: Boolean) -> Unit)? = null,
    showIcon: (@Composable (action: () -> Unit) -> Unit)? = { action ->
        Icon(
            if (showValue) Icons.Default.Visibility else Icons.Default.VisibilityOff,
            null,
            Modifier.padding(horizontal = 4.dp).clickable(onClick = action),
            LocalContentColor.current.orErrorColor(isError),
        )
    },
    timeIcon: @Composable (action: () -> Unit) -> Unit = { action ->
        Icon(
            Icons.Default.DateRange,
            null,
            Modifier.padding(horizontal = 4.dp).clickable(onClick = action),
        )
    },
    enumIcon: @Composable (action: () -> Unit) -> Unit = { action ->
        Icon(
            Icons.Filled.ArrowDropDown,
            null,
            Modifier.padding(horizontal = 4.dp).clickable(onClick = action),
        )
    },
    matchAllIcon: @Composable (value: Boolean, action: () -> Unit) -> Unit = { value, action ->
        Icon(
            painterResource(Res.drawable.whole_word),
            null,
            Modifier.padding(horizontal = 4.dp).clickable(onClick = action),
            LocalContentColor.current.orErrorColor(!value),
        )
    },
    regexMatchIcon: @Composable (value: Boolean, action: () -> Unit) -> Unit = { value, action ->
        Icon(
            painterResource(Res.drawable.regex),
            null,
            Modifier.padding(horizontal = 4.dp).clickable(onClick = action),
            LocalContentColor.current.orErrorColor(!value),
        )
    },
    ignoreCaseIcon: @Composable (value: Boolean, action: () -> Unit) -> Unit = { value, action ->
        Icon(
            painterResource(Res.drawable.case_sensitive),
            null,
            Modifier.padding(horizontal = 4.dp).clickable(onClick = action),
            LocalContentColor.current.orErrorColor(!value),
        )
    },
    compareIcon: @Composable (value: SearchFieldCompare, action: () -> Unit) -> Unit = { value, action ->
        Icon(
            when (value) {
                SearchFieldCompare.EQUALS -> LineAwesomeIcons.EqualsSolid
                SearchFieldCompare.NOT_EQUALS -> LineAwesomeIcons.NotEqualSolid
                SearchFieldCompare.LESS_THAN -> LineAwesomeIcons.LessThanSolid
                SearchFieldCompare.LESS_THAN_EQUAL -> LineAwesomeIcons.LessThanEqualSolid
                SearchFieldCompare.GREATER_THAN_EQUAL -> LineAwesomeIcons.GreaterThanEqualSolid
                SearchFieldCompare.GREATER_THAN -> LineAwesomeIcons.GreaterThanSolid
                SearchFieldCompare.BETWEEN -> LineAwesomeIcons.MinusSolid
            },
            null,
            Modifier.padding(horizontal = 4.dp).clickable(onClick = action),
        )
    },
    clearIcon: @Composable (action: () -> Unit) -> Unit = { action ->
        Icon(
            Icons.Default.Close,
            null,
            Modifier.padding(horizontal = 4.dp).clickable(onClick = action),
            MaterialTheme.colorScheme.error,
        )
    },
    prefix: @Composable ((isError: Boolean) -> Unit)? = null,
    suffix: @Composable ((isError: Boolean) -> Unit)? = null,
    supportingText: @Composable ((isError: Boolean) -> Unit)? = null,
    visualTransformation: VisualTransformation? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE, minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = OutlinedTextFieldDefaults.shape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    type: TextField = TextField.Text,
    clearable: Boolean = true,
    outlined: Boolean = false,
    validator: Validator? = null,
    underlineMessage: String? = null,
    onValidation: @Composable (List<String>) -> String = { value -> value.joinToString(", ") },
    matchAll: Boolean = true,
    regexMatch: Boolean = true,
    ignoreCase: Boolean = false,
    compareMatchers: List<SearchFieldCompare> = listOf(
        SearchFieldCompare.EQUALS,
        SearchFieldCompare.NOT_EQUALS,
        SearchFieldCompare.LESS_THAN_EQUAL,
        SearchFieldCompare.LESS_THAN,
        SearchFieldCompare.GREATER_THAN,
        SearchFieldCompare.GREATER_THAN_EQUAL,
        SearchFieldCompare.BETWEEN,
    )
): Unit = TextField(
    modifier,
    state.query,
    { state.query = it },
    isError,
    showValue,
    onShowValueChange,
    enabled,
    false,
    textStyle,
    label,
    placeholder,
    leadingIcon,
    {
        val isTime = type is TextField.LocalTime || type is TextField.LocalDate || type is TextField.LocalDateTime

        val isEnum = type is TextField.Enum

        if (state.compareMatch == SearchFieldCompare.BETWEEN && isTime) {
            var showTimePicker by remember { mutableStateOf(false) }

            if (showTimePicker) {

                var localDate: LocalDate? = null
                var localTime: LocalTime? = null

                when (type) {
                    TextField.LocalTime -> localTime =
                        state.query.ifEmpty { null }?.let(LocalTime::parse) ?: LocalTime.now(
                            TimeZone.currentSystemDefault(),
                        )

                    TextField.LocalDate -> localDate =
                        state.query.ifEmpty { null }?.let(LocalDate::parse) ?: LocalDate.now(
                            TimeZone.currentSystemDefault(),
                        )

                    TextField.LocalDateTime -> state.query.ifEmpty { null }?.let(LocalDateTime::parse).let {
                        it ?: LocalDateTime.now(
                            TimeZone.currentSystemDefault(),
                        )
                    }.let {
                        localTime = it.time
                        localDate = it.date
                    }

                    else -> {}
                }

                val datePickerState = localDate?.let { rememberDatePickerState(it.toEpochMilliseconds()) }

                val timePickerState = localTime?.let { rememberTimePickerState(it.hour, it.minute, true) }

                TimePickerDialog(
                    timePickerState = timePickerState,
                    datePickerState = datePickerState,
                    onDismissRequest = { showTimePicker = false },
                    confirmButton = {
                        IconButton(
                            onClick = {
                                if (state.query.isNotEmpty()) {
                                    timePickerStateToTime(
                                        datePickerState, timePickerState,
                                    )?.let { state.query = "${state.query.substringBefore("..")}..$it" }
                                }
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.Green,
                            )
                        }
                    },
                    dismissButton = {
                        IconButton(
                            onClick = {
                                showTimePicker = false
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                            )
                        }
                    },
                )
            }
            timeIcon { showTimePicker = !showTimePicker }
        }

        if (!(isTime || isEnum)) {
            if (matchAll) matchAllIcon(state.matchAll) { state.matchAll = !state.matchAll }

            if (regexMatch) regexMatchIcon(state.regexMatch) { state.regexMatch = !state.regexMatch }

            if (ignoreCase) ignoreCaseIcon(state.ignoreCase) { state.ignoreCase = !state.ignoreCase }
        }

        if (!isEnum && compareMatchers.isNotEmpty()) {
            var index by remember { mutableIntStateOf(0) }
            compareIcon(state.compareMatch) {
                if (++index >= compareMatchers.size) {
                    if (state.compareMatch == SearchFieldCompare.BETWEEN) {
                        state.query = ""
                    }
                    state.compareMatch = compareMatchers[0]
                    index = 0
                }
                else {
                    state.compareMatch = compareMatchers[index]
                }
            }
        }

        trailingIcon?.invoke(it)
    },
    showIcon,
    timeIcon,
    enumIcon,
    clearIcon,
    prefix,
    suffix,
    supportingText,
    visualTransformation,
    keyboardOptions,
    keyboardActions,
    singleLine,
    maxLines,
    minLines,
    interactionSource,
    shape,
    colors,
    type,
    true,
    clearable,
    outlined,
    underlineMessage,
    validator,
    onValidation,
    false,
)
