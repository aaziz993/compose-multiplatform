package clib.data.type.primitives.string

import androidx.compose.runtime.Composable
import klib.data.type.primitives.string.case.toSnakeCase
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Suppress("ComposeUnstableReceiver")
@Composable
public fun String.asStringResource(
    resources: Map<String, StringResource>,
    defaultValue: () -> String = { this }
): String = resources[lowercase().toSnakeCase()]?.let { stringResource -> stringResource(stringResource) }
    ?: defaultValue()
