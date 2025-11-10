package data.type.primitives.string.humanreadable

import androidx.compose.runtime.Composable
import clib.data.type.primitives.string.humanreadable.toHumanReadableSize
import com.ionspin.kotlin.bignum.integer.BigInteger
import compose_app.generated.resources.Res
import compose_app.generated.resources.size_suffixes

@Suppress("ComposeUnstableReceiver")
@Composable
public fun BigInteger.toHumanReadableSize(
    decimals: Int = 0,
    groupSeparator: String = ".",
    decimalSymbol: String = ",",
): String = toHumanReadableSize(
    decimals,
    groupSeparator,
    decimalSymbol,
    Res.array.size_suffixes,
)
