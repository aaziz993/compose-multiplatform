package klib.data.type.serialization.serializer

import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor


public object OptionalAnySerializer : GenericAnySerializer<Any?>() {

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("OptionalAny", PrimitiveKind.STRING)
}