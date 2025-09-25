package klib.data.type.primitives.string.humanreadable

import com.ionspin.kotlin.bignum.decimal.BigDecimal

/**
 * Formats the given number.

 * For example: 1_000_000.34 returns:
 * - "1,000,000.34" for EN
 * - "1 000 000.34" for FR
 * - "1.000.000,34" for NL
 */
public fun BigDecimal.toHumanReadable(
    decimals: Int = 0,
    groupSeparator: String = ".",
    decimalSymbol: String = "."
): String {

    val rounded = formatWithDecimals(decimals)
    val parts = rounded.split('.')

    // Format the integer part
    val formattedIntegerPart = parts[0]
        .reversed()
        .chunked(3)
        .joinToString(groupSeparator)
        .reversed()

    // Format the decimal part
    val decimalPart = if (parts.size > 1) parts[1] else ""
    val formattedDecimalPart = if (decimals > 0) {
        val truncatedDecimals = decimalPart.padEnd(decimals, '0').substring(0, decimals)
        decimalSymbol + truncatedDecimals
    }
    else {
        ""
    }

    return formattedIntegerPart + formattedDecimalPart
}

private fun BigDecimal.formatWithDecimals(decimals: Int): String {
    val multiplier = BigDecimal.TEN.pow(decimals)
    val numberAsString = (this * multiplier).toBigInteger().toString().padStart(decimals + 1, '0')
    val decimalIndex = numberAsString.length - decimals - 1
    val mainRes = numberAsString.substring(0..decimalIndex)
    val fractionRes = numberAsString.substring(decimalIndex + 1)
    return if (fractionRes.isEmpty()) {
        mainRes
    }
    else {
        "$mainRes.$fractionRes"
    }
}
