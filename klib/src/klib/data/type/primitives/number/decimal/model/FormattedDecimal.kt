package klib.data.type.primitives.number.decimal.model

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import klib.data.type.primitives.number.decimal.decimalLogger

/**
 * Result of formatting a decimal number, containing both display and parseable formats.
 * * @property displayValue The formatted string for display (e.g., "1.234,56" for European format)
 * * @property parseableValue The canonical string that can be parsed by numeric types (e.g., "1234.56")
 * * @property rawDigits The original input string containing only digits (e.g., "123456")
 * * Always uses dot as decimal separator and no thousand separators.
 */
public class FormattedDecimal internal constructor(
    public val displayValue: String,
    public val parseableValue: String,
    public val rawDigits: String
) {

    /**
     * Converts to BigDecimal. Returns null if conversion fails.
     */
    public fun toBigDecimalOrNull(): BigDecimal? = try {
        if (parseableValue == "0" || parseableValue.isEmpty()) BigDecimal.ZERO
        else parseableValue.toBigDecimal()
    }
    catch (e: NumberFormatException) {
        logError(e.message)
        null
    }

    /**
     * Converts to BigDecimal. Throws exception if conversion fails.
     */
    public fun toBigDecimal(): BigDecimal = toBigDecimalOrNull()
        ?: throw NumberFormatException("Cannot convert '$parseableValue' to BigDecimal")

    /**
     * Converts to Double. Returns null if conversion fails.
     */
    public fun toDoubleOrNull(): Double? = try {
        if (parseableValue == "0" || parseableValue.isEmpty()) 0.0
        else parseableValue.toDouble()
    }
    catch (e: NumberFormatException) {
        logError(e.message)
        null
    }

    /**
     * Converts to Double. Throws exception if conversion fails.
     */
    public fun toDouble(): Double = toDoubleOrNull()
        ?: throw NumberFormatException("Cannot convert '$parseableValue' to Double")

    /**
     * Converts to Float. Returns null if conversion fails.
     */
    public fun toFloatOrNull(): Float? = try {
        if (parseableValue == "0" || parseableValue.isEmpty()) 0.0f
        else parseableValue.toFloat()
    }
    catch (e: NumberFormatException) {
        logError(e.message)
        null
    }

    /**
     * Converts to Float. Throws exception if conversion fails.
     */
    public fun toFloat(): Float = toFloatOrNull()
        ?: throw NumberFormatException("Cannot convert '$parseableValue' to Float")

    /**
     * Returns the display value when used as a string
     */
    override fun toString(): String = displayValue

    private fun logError(message: String?) =
        decimalLogger.error { "Error converting '$parseableValue' to BigDecimal: $message" }
}
