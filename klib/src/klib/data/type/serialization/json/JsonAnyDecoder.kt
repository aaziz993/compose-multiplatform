package klib.data.type.serialization.json

import klib.data.type.serialization.coders.any.AnyDecoder
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonDecoder

internal class JsonAnyDecoder(decoder: JsonDecoder) : AnyDecoder<JsonDecoder>(decoder) {

    override fun decodeValue(): Any? = decoder.json.decodeAnyFromJsonElement(decoder.decodeJsonElement())

    override fun <T> decodeSerializableValue(deserializer: DeserializationStrategy<T>, value: Any?): T =
        decoder.json.decodeFromAny(deserializer, value)
}
