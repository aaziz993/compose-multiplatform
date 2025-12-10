package klib.data.location

import klib.data.iso.Alpha2Letter
import klib.data.location.country.Country
import klib.data.location.country.getCountries
import klib.data.location.country.toCountry
import klib.data.type.serialization.serializers.transform.TransformingSerializer
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable

@KeepGeneratedSerializer
@Serializable(PhoneSerializer::class)
public data class Phone(
    val countryCode: Alpha2Letter,
    val number: String,
) {

    override fun toString(): String = "+${countryCode.toCountry().dial}$number"
}

public fun String.toPhoneOrNull(): Phone? {
    val number = removePrefix("+")

    return Country
        .getCountries()
        .filterNot { country -> country.dial == null }
        .sortedBy { country -> country.dial!!.length }
        .find { country -> number.startsWith(country.dial!!) }
        ?.let { country ->
            Phone(country.alpha2, number.removePrefix(country.alpha2.toString()))
        }
}

public fun String.toPhone(): Phone = toPhoneOrNull() ?: error("Invalid ISO 3166-1 alpha-2 country code: $this")

private object PhoneSerializer : TransformingSerializer<Phone>(
    Phone.generatedSerializer(),
) {

    override fun transformDeserialize(value: Any?): Any? = (value as? String)?.toPhone() ?: value
}
