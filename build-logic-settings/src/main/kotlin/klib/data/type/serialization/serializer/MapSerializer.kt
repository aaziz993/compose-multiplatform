@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE", "BOUNDS_NOT_ALLOWED_IF_BOUNDED_BY_TYPE_PARAMETER")

package klib.data.type.serialization.serializer

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.elementNames
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.internal.LinkedHashMapClassDesc
import kotlin.collections.set

public open class MapSerializer<V, C : Map<String, V>, B>(
    private val tSerializer: KSerializer<B>,
    private val valueSerializer: KSerializer<V>,
) : KSerializer<C> where B : C, B : MutableMap<String, V> {

    override val descriptor: SerialDescriptor = tSerializer.descriptor

    @Suppress("UNCHECKED_CAST")
    override fun serialize(encoder: Encoder, value: C) = MapEncoder(encoder, valueSerializer).let { mapEncoder ->
        mapEncoder.unknownKeys = value
        tSerializer.serialize(mapEncoder, value as B)
    }

    override fun deserialize(decoder: Decoder): C = MapDecoder(decoder, valueSerializer).let { mapDecoder ->
        tSerializer.deserialize(mapDecoder).apply {
            putAll(mapDecoder.unknownKeys)
        }
    }
}

private class MapEncoder<V>(
    private val encoder: Encoder, private val valueSerializer: KSerializer<V>
) : Encoder by encoder {

    private val mapDescriptor = LinkedHashMapClassDesc(String.serializer().descriptor, valueSerializer.descriptor)

    lateinit var unknownKeys: Map<String, V>

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder = OriginCompositeEncoder()

    private inner class OriginCompositeEncoder(
        private val compositeEncoder: CompositeEncoder = encoder.beginStructure(mapDescriptor)
    ) : CompositeEncoder by compositeEncoder {

        override fun endStructure(descriptor: SerialDescriptor) {
            val elementsOffset = descriptor.elementsCount * 2
            unknownKeys.entries.forEachIndexed { index, (key, value) ->
                val offset = elementsOffset + index * 2
                compositeEncoder.encodeStringElement(mapDescriptor, offset, key)
                compositeEncoder.encodeSerializableElement(mapDescriptor, offset + 1, valueSerializer, value)
            }

            compositeEncoder.endStructure(mapDescriptor)
        }

        override fun encodeBooleanElement(descriptor: SerialDescriptor, index: Int, value: Boolean) {
            val encodeElementIndex = encodeElementIndex(descriptor, index)
            compositeEncoder.encodeBooleanElement(mapDescriptor, encodeElementIndex, value)
        }

        override fun encodeByteElement(descriptor: SerialDescriptor, index: Int, value: Byte) {
            val encodeElementIndex = encodeElementIndex(descriptor, index)
            compositeEncoder.encodeByteElement(mapDescriptor, encodeElementIndex, value)
        }

        override fun encodeShortElement(descriptor: SerialDescriptor, index: Int, value: Short) {
            val encodeElementIndex = encodeElementIndex(descriptor, index)
            compositeEncoder.encodeShortElement(mapDescriptor, encodeElementIndex, value)
        }

        override fun encodeCharElement(descriptor: SerialDescriptor, index: Int, value: Char) {
            val encodeElementIndex = encodeElementIndex(descriptor, index)
            compositeEncoder.encodeCharElement(mapDescriptor, encodeElementIndex, value)
        }

        override fun encodeIntElement(descriptor: SerialDescriptor, index: Int, value: Int) {
            val encodeElementIndex = encodeElementIndex(descriptor, index)
            compositeEncoder.encodeIntElement(mapDescriptor, encodeElementIndex, value)
        }

        override fun encodeLongElement(descriptor: SerialDescriptor, index: Int, value: Long) {
            val encodeElementIndex = encodeElementIndex(descriptor, index)
            compositeEncoder.encodeLongElement(mapDescriptor, encodeElementIndex, value)
        }

        override fun encodeFloatElement(descriptor: SerialDescriptor, index: Int, value: Float) {
            val encodeElementIndex = encodeElementIndex(descriptor, index)
            compositeEncoder.encodeFloatElement(mapDescriptor, encodeElementIndex, value)
        }

        override fun encodeDoubleElement(descriptor: SerialDescriptor, index: Int, value: Double) {
            val encodeElementIndex = encodeElementIndex(descriptor, index)
            compositeEncoder.encodeDoubleElement(mapDescriptor, encodeElementIndex, value)
        }

        override fun encodeStringElement(descriptor: SerialDescriptor, index: Int, value: String) {
            val encodeElementIndex = encodeElementIndex(descriptor, index)
            compositeEncoder.encodeStringElement(mapDescriptor, encodeElementIndex, value)
        }

        override fun encodeInlineElement(descriptor: SerialDescriptor, index: Int): Encoder {
            val encodeElementIndex = encodeElementIndex(descriptor, index)
            return compositeEncoder.encodeInlineElement(mapDescriptor, encodeElementIndex)
        }

        override fun <T : Any?> encodeSerializableElement(
            descriptor: SerialDescriptor,
            index: Int,
            serializer: SerializationStrategy<T>,
            value: T
        ) {
            val encodeElementIndex = encodeElementIndex(descriptor, index)
            compositeEncoder.encodeSerializableElement(mapDescriptor, encodeElementIndex, serializer, value)
        }

        override fun <T : Any> encodeNullableSerializableElement(
            descriptor: SerialDescriptor,
            index: Int,
            serializer: SerializationStrategy<T>,
            value: T?
        ) {
            val encodeElementIndex = encodeElementIndex(descriptor, index)
            compositeEncoder.encodeNullableSerializableElement(mapDescriptor, encodeElementIndex, serializer, value)
        }

        private fun encodeElementIndex(descriptor: SerialDescriptor, index: Int): Int =
            (index * 2).let { encodeElementIndex ->
                compositeEncoder.encodeStringElement(
                        mapDescriptor, encodeElementIndex, descriptor.getElementName(index),
                )
                encodeElementIndex + 1
            }
    }
}

private class MapDecoder<V>(
    private val decoder: Decoder, private val valueSerializer: KSerializer<V>
) : Decoder by decoder {

    private val mapDescriptor = LinkedHashMapClassDesc(String.serializer().descriptor, valueSerializer.descriptor)

    val unknownKeys: Map<String, V>
        field = LinkedHashMap<String, V>()

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder = OriginCompositeDecoder()

    private inner class OriginCompositeDecoder(
        private val compositeDecoder: CompositeDecoder = decoder.beginStructure(mapDescriptor)
    ) : CompositeDecoder by compositeDecoder {

        override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
            while (true) {
                val index = compositeDecoder.decodeElementIndex(mapDescriptor)

                if (index == CompositeDecoder.DECODE_DONE) {
                    return index
                }

                val key = compositeDecoder.decodeStringElement(mapDescriptor, index)

                val encodeElementIndex = descriptor.elementNames.indexOf(key)

                if (encodeElementIndex != -1) {
                    return encodeElementIndex
                }

                decodeElementIndex()

                val value = compositeDecoder.decodeSerializableElement(
                        mapDescriptor, index + 1, valueSerializer,
                )

                unknownKeys[key] = value
            }
        }

        override fun decodeBooleanElement(descriptor: SerialDescriptor, index: Int): Boolean =
            compositeDecoder.decodeBooleanElement(mapDescriptor, decodeElementIndex())

        override fun decodeByteElement(descriptor: SerialDescriptor, index: Int): Byte =
            compositeDecoder.decodeByteElement(mapDescriptor, decodeElementIndex())

        override fun decodeCharElement(descriptor: SerialDescriptor, index: Int): Char =
            compositeDecoder.decodeCharElement(mapDescriptor, decodeElementIndex())

        override fun decodeShortElement(descriptor: SerialDescriptor, index: Int): Short =
            compositeDecoder.decodeShortElement(mapDescriptor, decodeElementIndex())

        override fun decodeIntElement(descriptor: SerialDescriptor, index: Int): Int =
            compositeDecoder.decodeIntElement(mapDescriptor, decodeElementIndex())

        override fun decodeLongElement(descriptor: SerialDescriptor, index: Int): Long =
            compositeDecoder.decodeLongElement(mapDescriptor, decodeElementIndex())

        override fun decodeFloatElement(descriptor: SerialDescriptor, index: Int): Float =
            compositeDecoder.decodeFloatElement(mapDescriptor, decodeElementIndex())

        override fun decodeDoubleElement(descriptor: SerialDescriptor, index: Int): Double =
            compositeDecoder.decodeDoubleElement(mapDescriptor, decodeElementIndex())

        override fun decodeStringElement(descriptor: SerialDescriptor, index: Int): String =
            compositeDecoder.decodeStringElement(mapDescriptor, decodeElementIndex())

        override fun decodeInlineElement(descriptor: SerialDescriptor, index: Int): Decoder =
            compositeDecoder.decodeInlineElement(mapDescriptor, decodeElementIndex())

        override fun <T : Any?> decodeSerializableElement(
            descriptor: SerialDescriptor, index: Int, deserializer: DeserializationStrategy<T>, previousValue: T?
        ): T = compositeDecoder.decodeSerializableElement(
                mapDescriptor, decodeElementIndex(), deserializer, previousValue,
        )

        override fun <T : Any> decodeNullableSerializableElement(
            descriptor: SerialDescriptor, index: Int, deserializer: DeserializationStrategy<T?>, previousValue: T?
        ): T? = compositeDecoder.decodeNullableSerializableElement(
                mapDescriptor, decodeElementIndex(), deserializer, previousValue,
        )

        private fun decodeElementIndex() = compositeDecoder.decodeElementIndex(mapDescriptor)
    }
}
