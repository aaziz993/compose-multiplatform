package clib.data.type.primitives.string.humanreadable

import androidx.compose.runtime.Composable
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import klib.data.type.primitives.string.humanreadable.toHumanReadableAbbreviation
import org.jetbrains.compose.resources.StringArrayResource
import org.jetbrains.compose.resources.stringArrayResource

@Suppress("ComposeUnstableReceiver", "ComposeParameterOrder")
@Composable
public fun BigDecimal.toHumanReadableAbbreviation(
    decimals: Int = 0,
    groupSeparator: String = ".",
    decimalSymbol: String = ",",
    suffixes: StringArrayResource
): String = toHumanReadableAbbreviation(
    decimals,
    groupSeparator,
    decimalSymbol,
    stringArrayResource(suffixes),
)
