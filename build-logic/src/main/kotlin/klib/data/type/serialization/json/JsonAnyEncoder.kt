package klib.data.type.serialization.json

import klib.data.type.serialization.coders.any.AnyEncoder
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.JsonEncoder

internal class JsonAnyEncoder(encoder: JsonEncoder) : AnyEncoder<JsonEncoder>(encoder) {

    override fun <T> encodeSerializableValue(serializer: SerializationStrategy<T>, value: T): Any? =
        encoder.json.encodeToAny(serializer, value)

    override fun encodeValue(value: Any?): Unit =
        encoder.encodeJsonElement(encoder.json.encodeAnyToJsonElement(value))
}
