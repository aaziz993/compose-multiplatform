package clib.presentation.components.textfield.search

import clib.presentation.components.textfield.AdvancedTextField
import clib.presentation.components.textfield.model.TextField
import clib.presentation.components.textfield.search.model.SearchFieldCompare
import clib.presentation.components.textfield.search.model.SearchFieldState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import clib.generated.resources.Res
import clib.generated.resources.case_sensitive
import clib.generated.resources.regex
import clib.generated.resources.whole_word
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.EqualsSolid
import compose.icons.lineawesomeicons.GreaterThanEqualSolid
import compose.icons.lineawesomeicons.GreaterThanSolid
import compose.icons.lineawesomeicons.LessThanEqualSolid
import compose.icons.lineawesomeicons.LessThanSolid
import compose.icons.lineawesomeicons.MinusSolid
import compose.icons.lineawesomeicons.NotEqualSolid
import clib.presentation.state.timePickerStateToTemporal
import klib.data.type.primitives.time.now
import klib.data.type.primitives.time.toEpochMilliseconds
import klib.data.validator.Validator
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import org.jetbrains.compose.resources.painterResource
import clib.presentation.theme.model.color
import clib.presentation.components.dialog.time.AdvancedTimePickerDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun AdvancedSearchField(
    state: SearchFieldState,
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier.padding(horizontal = 2.dp),
    enabled: Boolean = true,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable ((isError: Boolean) -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable ((isError: Boolean) -> Unit)? = null,
    trailingIcon: @Composable ((isError: Boolean) -> Unit)? = null,
    prefix: @Composable ((isError: Boolean) -> Unit)? = null,
    suffix: @Composable ((isError: Boolean) -> Unit)? = null,
    supportingText: @Composable ((isError: Boolean) -> Unit)? = null,
    isError: Boolean = false,
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
    outlined: Boolean = false, validator: Validator? = null,
    onValidation: (List<String>) -> String = { it.joinToString(", ") },
    showValue: Boolean = true, onShowValueChange: ((Boolean) -> Unit)? = null,
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
): Unit = AdvancedTextField(
    modifier,
    iconModifier,
    state.query,
    { state.query = it },
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
            var showTemporalPicker by remember { mutableStateOf(false) }

            if (showTemporalPicker) {

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

                AdvancedTimePickerDialog(
                    onConfirm = { _, _ ->
                        if (state.query.isNotEmpty()) {
                            timePickerStateToTemporal(
                                datePickerState, timePickerState,
                            )?.let { state.query = "${state.query.substringBefore("..")}..$it" }
                        }
                    },
                    timePickerState = timePickerState,
                    datePickerState = datePickerState,
                    onCancel = { showTemporalPicker = false },
                )
            }
            Icon(
                Icons.Default.DateRange,
                "Select date",
                iconModifier.clickable { showTemporalPicker = !showTemporalPicker },
            )
        }

        if (!(isTime || isEnum)) {
            if (ignoreCase) {
                Icon(
                    painterResource(Res.drawable.case_sensitive),
                    null,
                    iconModifier.clickable { state.ignoreCase = !state.ignoreCase },
                    color(!state.ignoreCase),
                )
            }

            if (matchAll) {
                Icon(
                    painterResource(Res.drawable.whole_word),
                    null,
                    iconModifier.clickable { state.matchAll = !state.matchAll },
                    color(!state.matchAll),
                )
            }

            if (regexMatch) {
                Icon(
                    painterResource(Res.drawable.regex),
                    null,
                    iconModifier.clickable { state.regexMatch = !state.regexMatch },
                    color(!state.regexMatch),
                )
            }
        }

        if (!isEnum && compareMatchers.isNotEmpty()) {
            var index by remember { mutableStateOf(0) }

            Icon(
                when (state.compareMatch) {
                    SearchFieldCompare.EQUALS -> LineAwesomeIcons.EqualsSolid
                    SearchFieldCompare.NOT_EQUALS -> LineAwesomeIcons.NotEqualSolid
                    SearchFieldCompare.LESS_THAN -> LineAwesomeIcons.LessThanSolid
                    SearchFieldCompare.LESS_THAN_EQUAL -> LineAwesomeIcons.LessThanEqualSolid
                    SearchFieldCompare.GREATER_THAN_EQUAL -> LineAwesomeIcons.GreaterThanEqualSolid
                    SearchFieldCompare.GREATER_THAN -> LineAwesomeIcons.GreaterThanSolid
                    SearchFieldCompare.BETWEEN -> LineAwesomeIcons.MinusSolid
                },
                null,
                iconModifier.clickable {
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
                },
            )
        }

        trailingIcon?.invoke(it)
    },
    prefix,
    suffix,
    supportingText,
    isError,
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
    validator,
    onValidation,
    false,
    showValue,
    onShowValueChange,
)
