package klib.data.type.primitives.number.decimal.formatter

import klib.data.type.primitives.number.decimal.model.DecimalInputMode
import klib.data.type.primitives.number.decimal.model.DecimalSeparator
import klib.data.type.primitives.number.decimal.model.ThousandSeparator

/**
 * Configuration for decimal number formatting.
 *
 * @property decimalSeparator The character used to separate decimals
 * @property thousandSeparator The character used to separate thousands
 * @property decimalPlaces The number of decimal places to display
 * @property maxDigits Maximum number of digits allowed
 * @property prefix String to prepend to the formatted number (e.g., "$")
 * @property suffix String to append to the formatted number (e.g., " USD")
 * @property inputMode How input digits should be interpreted (FRACTIONAL or FIXED_DECIMALS)
 */
public data class DecimalFormatterConfiguration(
    val decimalSeparator: DecimalSeparator = DecimalSeparator.COMMA,
    val thousandSeparator: ThousandSeparator = ThousandSeparator.DOT,
    val decimalPlaces: Int = 2,
    val maxDigits: Int = 10,
    val prefix: String = "",
    val suffix: String = "",
    val inputMode: DecimalInputMode = DecimalInputMode.FRACTIONAL
) {

    init {
        require(maxDigits > 0) { "Max digits must be higher than 0" }
        require(decimalPlaces >= 0) { "Decimal places must be non-negative" }
        require(decimalPlaces < maxDigits) { "Decimal places must be less than max digits" }
        require(
            thousandSeparator == ThousandSeparator.NONE ||
                thousandSeparator.char != decimalSeparator.char,
        ) {
            "Thousand separator and decimal separator must be different"
        }
    }

    public companion object {

        public val DefaultConfiguration: DecimalFormatterConfiguration = DecimalFormatterConfiguration()

        /**
         * European format: 1.234,56 (dot for thousands, comma for decimals)
         */
        public fun european(
            decimalPlaces: Int = 2,
            maxDigits: Int = 10,
            prefix: String = "",
            suffix: String = "",
            inputMode: DecimalInputMode = DecimalInputMode.FRACTIONAL
        ): DecimalFormatterConfiguration = DecimalFormatterConfiguration(
            decimalSeparator = DecimalSeparator.COMMA,
            thousandSeparator = ThousandSeparator.DOT,
            decimalPlaces = decimalPlaces,
            maxDigits = maxDigits,
            prefix = prefix,
            suffix = suffix,
            inputMode = inputMode,
        )

        /**
         * US format: 1,234.56 (comma for thousands, dot for decimals)
         */
        public fun us(
            decimalPlaces: Int = 2,
            maxDigits: Int = 10,
            prefix: String = "",
            suffix: String = "",
            inputMode: DecimalInputMode = DecimalInputMode.FRACTIONAL
        ): DecimalFormatterConfiguration = DecimalFormatterConfiguration(
            decimalSeparator = DecimalSeparator.DOT,
            thousandSeparator = ThousandSeparator.COMMA,
            decimalPlaces = decimalPlaces,
            maxDigits = maxDigits,
            prefix = prefix,
            suffix = suffix,
            inputMode = inputMode,
        )

        /**
         * Swiss format: 1'234.56 (apostrophe for thousands, dot for decimals)
         */
        public fun swiss(
            decimalPlaces: Int = 2,
            maxDigits: Int = 10,
            prefix: String = "",
            suffix: String = "",
            inputMode: DecimalInputMode = DecimalInputMode.FRACTIONAL
        ): DecimalFormatterConfiguration = DecimalFormatterConfiguration(
            decimalSeparator = DecimalSeparator.DOT,
            thousandSeparator = ThousandSeparator.APOSTROPHE,
            decimalPlaces = decimalPlaces,
            maxDigits = maxDigits,
            prefix = prefix,
            suffix = suffix,
            inputMode = inputMode,
        )

        /**
         * No separators: 1234.56
         */
        public fun plain(
            decimalPlaces: Int = 2,
            maxDigits: Int = 10,
            prefix: String = "",
            suffix: String = "",
            inputMode: DecimalInputMode = DecimalInputMode.FRACTIONAL
        ): DecimalFormatterConfiguration = DecimalFormatterConfiguration(
            decimalSeparator = DecimalSeparator.DOT,
            thousandSeparator = ThousandSeparator.NONE,
            decimalPlaces = decimalPlaces,
            maxDigits = maxDigits,
            prefix = prefix,
            suffix = suffix,
            inputMode = inputMode,
        )

        /**
         * US Dollar format: $1,234.56
         */
        public fun usd(
            decimalPlaces: Int = 2,
            maxDigits: Int = 10,
            inputMode: DecimalInputMode = DecimalInputMode.FRACTIONAL
        ): DecimalFormatterConfiguration = us(
            decimalPlaces = decimalPlaces,
            maxDigits = maxDigits,
            prefix = "$",
            inputMode = inputMode,
        )

        /**
         * Swiss Franc format: CHF 1'234.56
         */
        public fun chf(
            decimalPlaces: Int = 2,
            maxDigits: Int = 10,
            inputMode: DecimalInputMode = DecimalInputMode.FRACTIONAL
        ): DecimalFormatterConfiguration = swiss(
            decimalPlaces = decimalPlaces,
            maxDigits = maxDigits,
            prefix = "CHF ",
            inputMode = inputMode,
        )

        /**
         * Euro format: 1.234,56 €
         */
        public fun euro(
            decimalPlaces: Int = 2,
            maxDigits: Int = 10,
            inputMode: DecimalInputMode = DecimalInputMode.FRACTIONAL
        ): DecimalFormatterConfiguration = european(
            decimalPlaces = decimalPlaces,
            maxDigits = maxDigits,
            suffix = " €",
            inputMode = inputMode,
        )

        /**
         * Percentage format: 12.34%
         */
        public fun percentage(
            decimalPlaces: Int = 2,
            maxDigits: Int = 5,
            inputMode: DecimalInputMode = DecimalInputMode.FRACTIONAL
        ): DecimalFormatterConfiguration = us(
            decimalPlaces = decimalPlaces,
            maxDigits = maxDigits,
            suffix = "%",
            inputMode = inputMode,
        )
    }
}
