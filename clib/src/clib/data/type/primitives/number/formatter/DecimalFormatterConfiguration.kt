package clib.data.type.primitives.number.formatter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import klib.data.type.primitives.number.formatter.DecimalFormatter
import klib.data.type.primitives.number.formatter.DecimalFormatterConfiguration

@Composable
public fun rememberDecimalFormatter(
    configuration: DecimalFormatterConfiguration = DecimalFormatterConfiguration.DefaultConfiguration,
): DecimalFormatter {
    return remember(configuration) { DecimalFormatter(configuration) }
}
