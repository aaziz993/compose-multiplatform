package klib.data.type.serialization

import klib.data.type.serialization.encoder.deserialize
import klib.data.type.serialization.encoder.serialize
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer

public fun <T : Any> T.toAny(): Any =
    (this::class.serializer() as KSerializer<T>).serialize(this)!!

public inline fun <reified T : Any> Any.fromAny(): T =
    T::class.serializer().deserialize(this)

@Suppress("UNCHECKED_CAST")
public inline fun <reified T : Any> KSerializer<T>.copyFrom(value: T, block: Any.() -> Any = { this }): T =
    value.toAny().block().fromAny<T>()

public val DeserializationStrategy<*>.elementSerializer: DeserializationStrategy<*>?
    get() {
        val hackyDecoder = HackyDecoder()
        try {
            this@elementSerializer.deserialize(hackyDecoder)
        } catch (e: Exception) {
            // WAI
        }
        return hackyDecoder.elementSerialzier
    }

@OptIn(ExperimentalSerializationApi::class)
private class HackyDecoder : AbstractDecoder() {
    override val serializersModule: SerializersModule = EmptySerializersModule()
    var elementSerialzier: DeserializationStrategy<*>? = null

    override fun <T> decodeSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        deserializer: DeserializationStrategy<T>,
        previousValue: T?
    ): T {
        elementSerialzier = deserializer
        error("break")
    }

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int = 0
}