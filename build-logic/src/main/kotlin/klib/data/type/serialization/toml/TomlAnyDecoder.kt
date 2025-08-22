@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.type.serialization.toml

import klib.data.type.serialization.coders.any.AnyDecoder
import kotlinx.serialization.DeserializationStrategy
import net.peanuuutz.tomlkt.TomlDecoder

internal class TomlAnyDecoder(decoder: TomlDecoder) : AnyDecoder<TomlDecoder>(decoder) {

    override fun decodeValue(): Any? =
        decoder.toml.decodeAnyFromTomlElement(decoder.decodeTomlElement())

    override fun <T> decodeSerializableValue(deserializer: DeserializationStrategy<T>, value: Any?): T =
        decoder.toml.decodeFromAny(deserializer, value)
}
