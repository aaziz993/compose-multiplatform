package clib.data.type.primitives.string.humanreadable

import androidx.compose.runtime.Composable
import com.ionspin.kotlin.bignum.integer.BigInteger
import klib.data.type.primitives.string.humanreadable.toHumanReadableSize
import org.jetbrains.compose.resources.StringArrayResource
import org.jetbrains.compose.resources.stringArrayResource

@Suppress("ComposeUnstableReceiver", "ComposeParameterOrder")
@Composable
public fun BigInteger.toHumanReadableSize(
    decimals: Int = 0,
    groupSeparator: String = ".",
    decimalSymbol: String = ",",
    suffixes: StringArrayResource,
): String = toHumanReadableSize(
    decimals,
    groupSeparator,
    decimalSymbol,
    stringArrayResource(suffixes),
)
