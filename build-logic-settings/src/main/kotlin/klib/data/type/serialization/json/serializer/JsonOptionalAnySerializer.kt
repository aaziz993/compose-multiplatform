package klib.data.type.serialization.json.serializer

import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor

public object JsonOptionalAnySerializer : JsonGenericAnySerializer<Any?>() {

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("OptionalAny", PrimitiveKind.STRING)
}
