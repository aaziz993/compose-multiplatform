@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.type.serialization.toml

import klib.data.type.serialization.coders.any.AnyEncoder
import kotlinx.serialization.SerializationStrategy
import net.peanuuutz.tomlkt.TomlEncoder

internal class TomlAnyEncoder(encoder: TomlEncoder) : AnyEncoder<TomlEncoder>(encoder) {

    override fun <T> encodeSerializableValue(serializer: SerializationStrategy<T>, value: T): Any? =
        encoder.toml.encodeToAny(serializer, value)

    override fun encodeValue(value: Any?): Unit =
        encoder.encodeTomlElement(encoder.toml.encodeAnyToTomlElement(value))
}
