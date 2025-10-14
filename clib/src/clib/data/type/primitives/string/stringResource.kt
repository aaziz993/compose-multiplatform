package clib.data.type.primitives.string

import androidx.compose.runtime.Composable
import klib.data.type.primitives.string.case.toSnakeCase
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
public fun stringResource(value: String, resources: Map<String, StringResource>): String =
    resources[value.lowercase().toSnakeCase()]?.let { stringResource -> stringResource(stringResource) } ?: value
