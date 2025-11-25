package klib.data.type.primitives.string.humanreadable

import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.integer.BigInteger
import klib.data.type.primitives.number.toBigDecimal

private val SIZE_BASE = BigInteger.fromInt(1024)

/**
 * Returns the given bytes size in human-readable format. For example:
 * a size of 3_500_000 bytes returns "3.5 MB". Assumes base 1024.
 *
 * For example, 3_5000_000 bytes returns: "3.5 MB" for EN or "3.5 Mo" for FR.
 *
 * @param decimals The number of decimals to use in formatting.
 * @return a formatted string
 */
public fun BigInteger.toHumanReadableSize(
    decimals: Int = 0,
    groupSeparator: String = ".",
    decimalSymbol: String = ",",
    suffixes: List<String> = listOf("B", "KB", "MB", "GB", "TB", "PB", "EB"),
): String {
    if (this == BigInteger.ZERO) return "0${suffixes.first()}"

    var value = this
    var exponent = 0

    while (value >= SIZE_BASE && exponent < suffixes.lastIndex) {
        value = value / SIZE_BASE
        exponent++
    }

    return "${
        toBigDecimal().divide(SIZE_BASE.pow(exponent).toBigDecimal(), DecimalMode.US_CURRENCY)
            .toHumanReadable(decimals, groupSeparator, decimalSymbol)
    }${suffixes[exponent]}"
}
