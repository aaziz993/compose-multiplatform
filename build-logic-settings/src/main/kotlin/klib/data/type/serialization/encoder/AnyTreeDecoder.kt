@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE", "BOUNDS_NOT_ALLOWED_IF_BOUNDED_BY_TYPE_PARAMETER")

package klib.data.type.serialization.encoder

import klib.data.type.toNumberOrNull
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.internal.ListLikeDescriptor
import kotlinx.serialization.internal.MapLikeDescriptor
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

internal class AnyTreeDecoder(
    val value: Any?
) : TreeDecoder() {

    override val serializersModule: SerializersModule = EmptySerializersModule()

    override fun decodeAny(): Any? = value

    @ExperimentalSerializationApi
    override fun decodeNotNullMark(): Boolean = true

    @ExperimentalSerializationApi
    override fun decodeNull(): Nothing? = null

    override fun decodeBoolean(): Boolean = get()

    override fun decodeByte(): Byte = get()

    override fun decodeShort(): Short = get()

    override fun decodeChar(): Char = get()

    override fun decodeInt(): Int = get()

    override fun decodeLong(): Long = get()

    override fun decodeFloat(): Float = get()

    override fun decodeDouble(): Double = get()

    override fun decodeString(): String = get()

    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int = enumDescriptor.getElementIndex(value as String)

    override fun decodeInline(descriptor: SerialDescriptor): Decoder = this

    private inline fun <reified T : Any> get(): T = value?.toNumberOrNull() ?: value as T

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder =
        StructureCompositeDecoder(
            when {
                descriptor is ListLikeDescriptor -> value as List<*>

                descriptor is MapLikeDescriptor -> (value as Map<*, *>).flatMap { (key, value) ->
                    listOf(key, value)
                }

                value is Map<*, *> -> value.values

                else -> throw IllegalArgumentException()
            }.toTypedArray()
        )

    private inner class StructureCompositeDecoder(
        val array: Array<Any?>
    ) : CompositeDecoder {
        private var index = 0

        override val serializersModule: SerializersModule = EmptySerializersModule()

        override fun endStructure(descriptor: SerialDescriptor) {}

        override fun decodeElementIndex(descriptor: SerialDescriptor): Int =
            if (index < array.size) index++
            else CompositeDecoder.DECODE_DONE

        override fun decodeBooleanElement(descriptor: SerialDescriptor, index: Int): Boolean = get(index)

        override fun decodeByteElement(descriptor: SerialDescriptor, index: Int): Byte = get(index)

        override fun decodeCharElement(descriptor: SerialDescriptor, index: Int): Char = get(index)

        override fun decodeShortElement(descriptor: SerialDescriptor, index: Int): Short = get(index)

        override fun decodeIntElement(descriptor: SerialDescriptor, index: Int): Int = get(index)

        override fun decodeLongElement(descriptor: SerialDescriptor, index: Int): Long = get(index)

        override fun decodeFloatElement(descriptor: SerialDescriptor, index: Int): Float = get(index)

        override fun decodeDoubleElement(descriptor: SerialDescriptor, index: Int): Double = get(index)

        override fun decodeStringElement(descriptor: SerialDescriptor, index: Int): String = get(index)

        override fun decodeInlineElement(descriptor: SerialDescriptor, index: Int): Decoder =
            this@AnyTreeDecoder

        override fun <T : Any?> decodeSerializableElement(
            descriptor: SerialDescriptor, index: Int, deserializer: DeserializationStrategy<T>, previousValue: T?
        ): T = deserializer.deserialize(AnyTreeDecoder(get(index)))

        override fun <T : Any> decodeNullableSerializableElement(
            descriptor: SerialDescriptor, index: Int, deserializer: DeserializationStrategy<T?>, previousValue: T?
        ): T? = get<Any?>(index)?.let {
            decodeSerializableElement(descriptor, index, deserializer)
        }

        private inline fun <reified T> get(index: Int): T = array[index].let { value ->
            value?.toNumberOrNull() ?: value as T
        }
    }
}

public fun <T> DeserializationStrategy<T>.deserialize(value: Any?): T =
    deserialize(AnyTreeDecoder(value))
