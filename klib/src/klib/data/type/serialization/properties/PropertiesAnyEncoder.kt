package klib.data.type.serialization.properties

import klib.data.type.cast
import klib.data.type.serialization.coders.any.AnyEncoder
import kotlinx.serialization.SerializationStrategy

internal class PropertiesAnyEncoder(encoder: PropertiesEncoder) : AnyEncoder<PropertiesEncoder>(encoder) {

    override fun <T> encodeSerializableValue(serializer: SerializationStrategy<T>, value: T): Any? =
        encoder.properties.encodeToAny(serializer, value)

    override fun encodeValue(value: Any?): Unit = encoder.encodeAny(value.cast())
}
