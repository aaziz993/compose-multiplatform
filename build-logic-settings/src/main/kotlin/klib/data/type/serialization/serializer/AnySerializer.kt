package klib.data.type.serialization.serializer

import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor


public object AnySerializer : GenericAnySerializer<Any?>() {

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Any", PrimitiveKind.STRING)
}