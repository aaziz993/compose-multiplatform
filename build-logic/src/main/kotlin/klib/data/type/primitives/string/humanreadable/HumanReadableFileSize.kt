package klib.data.type.primitives.string.humanreadable

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger

public const val KB: Long = 1024

public const val MB: Long = 1048576

public const val GB: Long = 1073741824

public const val TB: Long = 1099511627776

public const val PB: Long = 1125899906842624

public const val EB: Long = 1152921504606846976

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
    vararg suffixes: Pair<BigInteger, String> = arrayOf(
        BigInteger.ONE to "B",
        KB.toBigInteger() to "KB",
        MB.toBigInteger() to "MB",
        GB.toBigInteger() to "GB",
        TB.toBigInteger() to "TB",
        PB.toBigInteger() to "PB",
        PB.toBigInteger() to "EB",
    )
): String {
    val index = suffixes.indexOfFirst { (value, _) -> this < value } - 1

    return "${
        (BigDecimal.fromBigInteger(this).divide(BigDecimal.fromBigInteger(suffixes[index].first), DecimalMode.US_CURRENCY))
            .toHumanReadable(decimals, groupSeparator, decimalSymbol)
    }${suffixes[index].second}"
}
