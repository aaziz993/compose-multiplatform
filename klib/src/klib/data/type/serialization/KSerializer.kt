package klib.data.type.serialization

import klib.data.type.collections.list.drop
import klib.data.type.collections.plus
import klib.data.type.collections.deepPlus
import klib.data.type.serialization.coders.tree.deserialize
import klib.data.type.serialization.coders.tree.serialize
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.internal.MapLikeSerializer
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer

public val KSerializer<*>.childSerializers: List<KSerializer<*>>
    get() = (0 until descriptor.elementsCount).map(transform = ::childSerializer)

public fun KSerializer<*>.childSerializer(index: Int): KSerializer<*> {
    if (this is MapLikeSerializer<*, *, *, *>)
        return listOf(keySerializer, valueSerializer)[index % 2]

    val elementDecoder = ElementDecoder(index)

    try {
        deserialize(elementDecoder)
    }
    catch (_: Exception) {
    }

    return elementDecoder.elementDeserializer
}

private class ElementDecoder(private val index: Int) : AbstractDecoder() {

    override val serializersModule: SerializersModule = EmptySerializersModule()
    lateinit var elementDeserializer: KSerializer<*>

    override fun decodeBoolean(): Boolean = decodeSerializer()
    override fun decodeByte(): Byte = decodeSerializer()
    override fun decodeShort(): Short = decodeSerializer()
    override fun decodeInt(): Int = decodeSerializer()
    override fun decodeLong(): Long = decodeSerializer()
    override fun decodeFloat(): Float = decodeSerializer()
    override fun decodeDouble(): Double = decodeSerializer()
    override fun decodeChar(): Char = decodeSerializer()
    override fun decodeString(): String = decodeSerializer()

    override fun <T> decodeSerializableValue(deserializer: DeserializationStrategy<T>): T =
        decodeSerializer(deserializer)

    override fun <T : Any> decodeNullableSerializableValue(deserializer: DeserializationStrategy<T?>): T? =
        decodeSerializer(deserializer)

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int = index

    private fun <T> decodeSerializer(deserializer: DeserializationStrategy<T>): T {
        elementDeserializer = deserializer as KSerializer<T>
        error("break")
    }

    private inline fun <reified T : Any> decodeSerializer(): T = decodeSerializer(T::class.serializer())
}

public fun <T : Any> KSerializer<T>.plus(
    vararg values: T,
    serializersModule: SerializersModule = EmptySerializersModule()
): T = values.map { value -> serialize(value, serializersModule)!! }.let { sources ->
    deserialize(
        sources.first().plus(*sources.drop().toTypedArray()),
        serializersModule,
    )
}

public fun <T : Any> KSerializer<T>.deepPlus(
    vararg values: T,
    serializersModule: SerializersModule = EmptySerializersModule(),
): T = values.map { value -> serialize(value, serializersModule)!! }.let { sources ->
    deserialize(
        sources.first().deepPlus(*sources.drop().toTypedArray()),
        serializersModule,
    )
}

public inline fun <T : Any> KSerializer<T>.deepCopy(
    value: T,
    serializersModule: SerializersModule = EmptySerializersModule(),
    transform: Any.() -> Any = { this }
): T = deserialize(serialize(value, serializersModule)!!.transform(), serializersModule)
