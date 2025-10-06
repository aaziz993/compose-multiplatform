package klib.data.location.currency

import klib.data.location.country.Alpha3Letter

// https://github.com/ourworldincode/currency/blob/main/currencies.json
public data class Currency(
    val name: String? = null,
    val demonym: Alpha3Letter,
    val majorSingle: String? = null,
    val majorPlural: String? = null,
    val ISOnum: Int,
    val symbol: String? = null,
    val symbolNative: String? = null,
    val minorSingle: String? = null,
    val minorPlural: String? = null,
    val ISOdigits: Int,
    val decimals: Int? = null,
    val numToBasic: Int? = null,
) {

    override fun toString(): String = demonym.toString()

    public companion object {

        // https://www.currency-iso.org/en/home/tables/table-a1.html
        public fun forCode(code: Alpha3Letter): Currency =
            forCodeOrNull(code) ?: error("Invalid ISO 4217 currency code: $code")


        public fun forCodeOrNull(code: Alpha3Letter): Currency? =
            CurrencyRegistry.currencies[code]


        public fun forCode(code: String): Currency =
            forCodeOrNull(Alpha3Letter(code)) ?: error("Invalid ISO 4217 currency code: $code")


        public fun forCodeOrNull(code: String): Currency? = forCodeOrNull(Alpha3Letter(code))
    }
}
