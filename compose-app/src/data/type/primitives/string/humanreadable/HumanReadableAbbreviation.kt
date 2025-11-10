package data.type.primitives.string.humanreadable

import androidx.compose.runtime.Composable
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import compose_app.generated.resources.Res
import clib.data.type.primitives.string.humanreadable.toHumanReadableAbbreviation
import compose_app.generated.resources.number_suffixes

@Suppress("ComposeUnstableReceiver")
@Composable
public fun BigDecimal.toHumanReadableAbbreviation(
    decimals: Int = 0,
    groupSeparator: String = ".",
    decimalSymbol: String = ",",
): String = toHumanReadableAbbreviation(
    decimals,
    groupSeparator,
    decimalSymbol,
    Res.array.number_suffixes,
)
