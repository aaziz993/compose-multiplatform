package clib.presentation.components.textfield

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import clib.data.type.orErrorColor
import clib.data.type.state.timePickerStateToTime
import clib.presentation.components.dialog.time.AdvancedTimePickerDialog
import clib.presentation.components.dropdown.list.ListDropdown
import clib.presentation.components.textfield.model.TextField
import klib.data.type.primitives.time.now
import klib.data.type.primitives.time.parseOrNull
import klib.data.type.primitives.time.toEpochMilliseconds
import klib.data.validator.Validator
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone

@Composable
public fun AdvancedTextField(
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier.padding(horizontal = 4.dp),
    value: String = "",
    onValueChange: (String) -> Unit = {},
    enabled: Boolean = true,
    readOnly: Boolean = false,
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
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = OutlinedTextFieldDefaults.shape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    type: TextField = TextField.Text,
    cursor: Boolean = false,
    clearable: Boolean = !readOnly,
    outlined: Boolean = false,
    underlineMessage: String? = null,
    validator: Validator? = null,
    onValidation: @Composable (List<String>) -> String = { value -> value.joinToString(", ") },
    showValidationMessage: Boolean = true,
    showValue: Boolean = true,
    onShowValueChange: ((Boolean) -> Unit)? = null
) {
    val isTime = when (type) {
        TextField.LocalTime, TextField.LocalDate, TextField.LocalDateTime -> true
        else -> false
    }
    val isEnum = type is TextField.Enum

    val requireValidation = !(readOnly || isTime || isEnum || validator == null)

    val validationMessages = if (requireValidation) validator.validate(value) else emptyList()

    val isErrorWithValidation = isError || validationMessages.isNotEmpty()

    val advancedOnValueChange = if (readOnly || isTime || type is TextField.Enum) {
        {}
    }
    else onValueChange

    val advancedLabel: (@Composable () -> Unit)? = label?.let { { it.invoke(isErrorWithValidation) } }

    val advancedLeadingIcon: (@Composable () -> Unit)? = leadingIcon?.let { { it.invoke(isErrorWithValidation) } }

    val clearIconButton: (@Composable () -> Unit)? = if (clearable && enabled && value.isNotBlank()) {
        {
            Icon(
                Icons.Default.Close, null, iconModifier.clickable { onValueChange("") }, MaterialTheme.colorScheme.error,
            )
        }
    }
    else null

    val showIconButton: (@Composable (isError: Boolean) -> Unit)? = onShowValueChange?.let { osc ->
        {
            Icon(
                if (showValue) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                null,
                iconModifier.clickable { osc(!showValue) },
                LocalContentColor.current.orErrorColor(it),
            )
        }
    }

    val timeIconButton: (@Composable () -> Unit)? = if (isTime && !readOnly) {
        var showTimePicker by remember { mutableStateOf(false) }

        if (showTimePicker) {

            var localDate: LocalDate? = null
            var localTime: LocalTime? = null

            when (type) {
                TextField.LocalTime -> localTime =
                    value.ifEmpty { null }?.let(LocalTime::parseOrNull) ?: LocalTime.now(
                        TimeZone.currentSystemDefault(),
                    )

                TextField.LocalDate -> localDate =
                    value.ifEmpty { null }?.let(LocalDate::parseOrNull) ?: LocalDate.now(
                        TimeZone.currentSystemDefault(),
                    )

                TextField.LocalDateTime -> value.ifEmpty { null }?.let(LocalDateTime::parseOrNull).let {
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
                    onValueChange(
                        timePickerStateToTime(datePickerState, timePickerState)?.toString().orEmpty(),
                    )
                },
                timePickerState = timePickerState,
                datePickerState = datePickerState,
                onCancel = { showTimePicker = false },
            )
        }
        {
            Icon(
                Icons.Default.DateRange,
                "Select date",
                iconModifier.clickable(onClick = { showTimePicker = !showTimePicker }),
            )

        }
    }
    else null

    var showEnumDropdown by remember { mutableStateOf(false) }

    val enumIconButton: (@Composable () -> Unit)? = if (isEnum && !readOnly) {
        {
            Icon(
                Icons.Filled.ArrowDropDown,
                null,
                iconModifier.clickable(onClick = { showEnumDropdown = !showEnumDropdown }),
            )
        }
    }
    else null

    val advancedTrailingIcon: (@Composable () -> Unit)? =
        if (!(clearIconButton == null && showIconButton == null && trailingIcon == null)) {
            {
                Row(Modifier.wrapContentSize(), verticalAlignment = Alignment.CenterVertically) {
                    showIconButton?.invoke(isErrorWithValidation)
                    timeIconButton?.invoke()
                    enumIconButton?.invoke()
                    clearIconButton?.invoke()
                    trailingIcon?.invoke(isErrorWithValidation)
                }
            }
        }
        else null

    val advancedPrefix: (@Composable () -> Unit)? = prefix?.let { { it.invoke(isErrorWithValidation) } }

    val advancedSuffix: (@Composable () -> Unit)? = suffix?.let { { it.invoke(isErrorWithValidation) } }

    val advancedSupportingText: (@Composable () -> Unit)? = supportingText?.let { { it.invoke(isErrorWithValidation) } }

    val advancedVisualTransformation = visualTransformation ?: if (showValue) VisualTransformation.None
    else PasswordVisualTransformation()

    val textField: @Composable () -> Unit = if (outlined) {
        {
            OutlinedTextField(
                value,
                advancedOnValueChange,
                modifier,
                enabled,
                readOnly && !cursor,
                textStyle,
                advancedLabel,
                placeholder,
                advancedLeadingIcon,
                advancedTrailingIcon,
                advancedPrefix,
                advancedSuffix,
                advancedSupportingText,
                isErrorWithValidation,
                advancedVisualTransformation,
                keyboardOptions,
                keyboardActions,
                singleLine,
                maxLines,
                minLines,
                interactionSource,
                shape,
                colors,
            )
        }
    }
    else {
        {
            TextField(
                value,
                advancedOnValueChange,
                modifier,
                enabled,
                readOnly && !cursor,
                textStyle,
                advancedLabel,
                placeholder,
                advancedLeadingIcon,
                advancedTrailingIcon,
                advancedPrefix,
                advancedSuffix,
                advancedSupportingText,
                isErrorWithValidation,
                advancedVisualTransformation,
                keyboardOptions,
                keyboardActions,
                singleLine,
                maxLines,
                minLines,
                interactionSource,
                shape,
                colors,
            )
        }
    }


    if (isEnum)
        return ListDropdown(
            type.values,
            textField,
            onValueChange,
            showEnumDropdown,
            { showEnumDropdown = false },
        )

    var message: String? = underlineMessage

    val vm = onValidation(validationMessages)
    if (vm.isNotEmpty() && showValidationMessage)
        message += message.orEmpty() + vm

    message?.let {
        Column(Modifier.wrapContentSize()) {
            textField()
            Text(it, color = if (isErrorWithValidation) MaterialTheme.colorScheme.error else Color.Unspecified)
        }
    } ?: textField()
}
