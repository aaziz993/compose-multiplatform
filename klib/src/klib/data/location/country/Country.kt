package klib.data.location.country

import klib.data.iso.Alpha2Letter
import klib.data.iso.Alpha3Letter
import klib.data.location.locale.Locale

public data class Country(
    val name: String,
    val alpha2: Alpha2Letter,
    val alpha3: Alpha3Letter,
    val countryCode: Int,
    val iso31662: String? = null,
    val region: String? = null,
    val subRegion: String? = null,
    val intermediateRegion: String? = null,
    val regionCode: Int? = null,
    val subRegionCode: Int? = null,
    val intermediateRegionCode: Int? = null,
    val dial: String? = null,
    val locales: Set<Locale> = emptySet(),
) {

    override fun toString(): String = alpha2.toString()

    public companion object {

        // https://www.iso.org/obp/ui/
        public fun forCode(code: Alpha2Letter): Country =
            forCodeOrNull(code) ?: error("Invalid ISO 3166-1 alpha-2 country code: $code")

        public fun forCode(code: String): Country =
            forCodeOrNull(Alpha2Letter.parse(code))
                ?: error("Invalid ISO 3166-1 alpha-2 country code: $code")

        public fun forCodeOrNull(code: Alpha2Letter): Country? =
            Country.getCountries().find { country -> country.alpha2 == code }

        public fun forCodeOrNull(code: String): Country? =
            Alpha2Letter.parseOrNull(code)?.let(::forCodeOrNull)

        public fun forLocaleOrNull(locale: Locale): Country? =
            Country.getCountries().find { country -> locale in country.locales }
                ?: Country.getCountries().find { country -> country.locales.any { it.language == locale.language } }

        public fun forLocale(locale: Locale): Country =
            forLocaleOrNull(locale) ?: error("Invalid locale: $locale")
    }
}

public fun Alpha2Letter.toCountry(): Country = Country.forCode(this)

public fun Alpha2Letter.toCountryOrNull(): Country? = Country.forCodeOrNull(this)

public fun String.toCountry(): Country = Country.forCode(this)

public fun String.toCountryOrNull(): Country? = Country.forCodeOrNull(this)
