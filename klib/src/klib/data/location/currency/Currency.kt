package klib.data.location.currency

import klib.data.iso.Alpha3Letter
import klib.data.type.serialization.serializers.primitive.PrimitiveStringSerializer
import kotlinx.serialization.Serializable

@Serializable(CurrencySerializer::class)
public data class Currency(
    val alpha3: Alpha3Letter,
    val name: String? = null,
    val demonym: String,
    val majorSingle: String? = null,
    val majorPlural: String? = null,
    val isoNum: Int? = null,
    val symbol: String? = null,
    val symbolNative: String? = null,
    val minorSingle: String? = null,
    val minorPlural: String? = null,
    val isoDigits: Int,
    val decimals: Int? = null,
    val numToBasic: Int? = null,
) {

    override fun toString(): String = alpha3.toString()

    public companion object {

        // https://www.currency-iso.org/en/home/tables/table-a1.html
        public fun forCode(code: Alpha3Letter): Currency =
            forCodeOrNull(code) ?: error("Invalid ISO 4217 currency code: $code")

        public fun forCodeOrNull(code: Alpha3Letter): Currency? =
            Currency.getCurrencies().find { currency -> currency.alpha3 == code }

        public fun forCode(code: String): Currency =
            forCodeOrNull(Alpha3Letter(code)) ?: error("Invalid ISO 4217 currency code: $code")

        public fun forCodeOrNull(code: String): Currency? = forCodeOrNull(Alpha3Letter(code))
    }
}

private object CurrencySerializer : PrimitiveStringSerializer<Currency>(
    "klib.data.location.currency.Currency",
    Currency::toString,
    String::toCurrency,
)

public fun String.toCurrency(): Currency = Currency.forCode(this)

public fun String.toCurrencyOrNull(): Currency? = Currency.forCodeOrNull(this)
