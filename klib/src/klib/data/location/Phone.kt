package klib.data.location

import klib.data.location.country.Country
import klib.data.location.country.getCountries
import klib.data.type.serialization.serializers.transform.TransformingSerializer
import klib.data.type.tuples.to
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable

@KeepGeneratedSerializer
@Serializable(PhoneSerializer::class)
public data class Phone(
    val dial: String,
    val number: String,
) {

    override fun toString(): String = "$dial$number"
}

private object PhoneSerializer : TransformingSerializer<Phone>(
    Phone.generatedSerializer(),
) {

    override fun transformDeserialize(value: Any?): Any? = (value as? String)?.phoneParts()?.let { (dial, number) ->
        mapOf(
            Phone::dial.name to dial,
            Phone::number.name to number,
        )
    } ?: value
}

public fun String.phoneParts(): Pair<String, String>? = Country
    .getCountries()
    .map(Country::dial)
    .filterNotNull()
    .sortedByDescending(String::length).toList()
    .find(::startsWith)?.to(::removePrefix)

public fun String.toPhoneOrNull(): Phone? = phoneParts()?.let { (dial, number) -> Phone(dial, number) }

public fun String.toPhone(): Phone = toPhoneOrNull() ?: error("Invalid phone number: $this")
