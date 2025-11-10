package klib.data.type.primitives.string.humanreadable

import com.ionspin.kotlin.bignum.decimal.BigDecimal

/**
 * Returns the given number in a short human-readable format.
 *
 * Supported abbreviations: K (1,000), M (1,000,000), B (1,000,000,000) and T (1,000,000,000,000).
 *
 * For example: 10394 returns "10K" and "4234321" returns "4M".
 */
public fun BigDecimal.toHumanReadableAbbreviation(
    decimals: Int = 0,
    groupSeparator: String = ".",
    decimalSymbol: String = ",",
    suffixes: List<String> = listOf("", "K", "M", "B", "T", "P", "Z", "Y", "Q")
): String {
    var current = this
    var index = 0

    while (current >= 1000 && index < suffixes.size - 1) {
        current /= 1000
        index++
    }

    return "${current.toHumanReadable(decimals, groupSeparator, decimalSymbol)}${suffixes[index]}"
}
