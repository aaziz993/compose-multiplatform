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
    val dial: String,
    val number: String,
) {

    override fun toString(): String = "+$dial$number"
}

private object PhoneSerializer : TransformingSerializer<Phone>(
    Phone.generatedSerializer(),
) {

    override fun transformDeserialize(value: Any?): Any? = (value as? String)?.toPhone() ?: value
}

public fun String.toPhoneOrNull(): Phone? {
    val number = removePrefix("+")

    return Country
        .getCountries()
        .filterNot { country -> country.dial.isNullOrEmpty() }
        .sortedByDescending { country -> country.dial!!.length }
        .find { country -> number.startsWith(country.dial!!) }
        ?.let { country ->
            Phone(country.dial!!, number.removePrefix(country.dial))
        }
}

public fun String.toPhone(): Phone = toPhoneOrNull() ?: error("Invalid phone number: $this")
