@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE", "BOUNDS_NOT_ALLOWED_IF_BOUNDED_BY_TYPE_PARAMETER")

package klib.data.type.serialization.encoder

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.internal.MapLikeDescriptor
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

@PublishedApi
internal class AnyTreeEncoder : Encoder {

    override val serializersModule: SerializersModule = EmptySerializersModule()

    var value: Any? = null
        private set

    override fun encodeNull() = set(null)

    override fun encodeBoolean(value: Boolean): Unit = set(value)

    override fun encodeByte(value: Byte): Unit = set(value)

    override fun encodeShort(value: Short): Unit = set(value)

    override fun encodeChar(value: Char): Unit = set(value)

    override fun encodeInt(value: Int): Unit = set(value)

    override fun encodeLong(value: Long): Unit = set(value)

    override fun encodeFloat(value: Float): Unit = set(value)

    override fun encodeDouble(value: Double): Unit = set(value)

    override fun encodeString(value: String): Unit = set(value)

    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) = set(enumDescriptor.getElementName(index))

    override fun encodeInline(descriptor: SerialDescriptor): Encoder =
        throw UnsupportedOperationException()

    private fun set(value: Any?) {
        this@AnyTreeEncoder.value = value
    }

    override fun beginCollection(descriptor: SerialDescriptor, collectionSize: Int): CompositeEncoder =
        CollectionCompositeEncoder(
            if (descriptor is MapLikeDescriptor) collectionSize * 2 else collectionSize
        ) { array ->
            value = if (descriptor is MapLikeDescriptor)
                array.toList().chunked(2).associate { (key, value) ->
                    key to value
                }
            else array.toList()
        }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder =
        StructureCompositeEncoder(serializersModule)

    private inner class CollectionCompositeEncoder(
        private val collectionSize: Int,
        private val _endStructure: (list: Array<Any?>) -> Unit
    ) : CompositeEncoder {
        private val array = Array<Any?>(collectionSize) { null }

        override val serializersModule: SerializersModule = EmptySerializersModule()

        override fun endStructure(descriptor: SerialDescriptor) = _endStructure(array)

        override fun encodeBooleanElement(
            descriptor: SerialDescriptor,
            index: Int,
            value: Boolean
        ): Unit = set(index, value)

        override fun encodeByteElement(
            descriptor: SerialDescriptor,
            index: Int,
            value: Byte
        ): Unit = set(index, value)

        override fun encodeShortElement(
            descriptor: SerialDescriptor,
            index: Int,
            value: Short
        ): Unit = set(index, value)

        override fun encodeCharElement(
            descriptor: SerialDescriptor,
            index: Int,
            value: Char
        ): Unit = set(index, value)

        override fun encodeIntElement(
            descriptor: SerialDescriptor,
            index: Int,
            value: Int
        ): Unit = set(index, value)

        override fun encodeLongElement(
            descriptor: SerialDescriptor,
            index: Int,
            value: Long
        ): Unit = set(index, value)

        override fun encodeFloatElement(
            descriptor: SerialDescriptor,
            index: Int,
            value: Float
        ): Unit = set(index, value)

        override fun encodeDoubleElement(
            descriptor: SerialDescriptor,
            index: Int,
            value: Double
        ): Unit = set(index, value)

        override fun encodeStringElement(
            descriptor: SerialDescriptor,
            index: Int,
            value: String
        ): Unit = set(index, value)

        override fun encodeInlineElement(
            descriptor: SerialDescriptor,
            index: Int
        ): Encoder = this@AnyTreeEncoder

        override fun <T> encodeSerializableElement(
            descriptor: SerialDescriptor,
            index: Int,
            serializer: SerializationStrategy<T>,
            value: T
        ) = set(index, serializer.encodeToAny(value))

        @ExperimentalSerializationApi
        override fun <T : Any> encodeNullableSerializableElement(
            descriptor: SerialDescriptor,
            index: Int,
            serializer: SerializationStrategy<T>,
            value: T?
        ): Unit = value?.let { value ->
            encodeSerializableElement(descriptor, index, serializer, value)
        } ?: set(index, null)

        private fun set(index: Int, value: Any?) {
            array[index] = value
        }
    }

    private inner class StructureCompositeEncoder(override val serializersModule: SerializersModule) :
        CompositeEncoder {
        private val map = mutableMapOf<String, Any?>()

        override fun endStructure(descriptor: SerialDescriptor) {
            value = map
        }

        override fun encodeBooleanElement(descriptor: SerialDescriptor, index: Int, value: Boolean): Unit =
            put(descriptor, index, value)

        override fun encodeByteElement(descriptor: SerialDescriptor, index: Int, value: Byte): Unit =
            put(descriptor, index, value)

        override fun encodeShortElement(descriptor: SerialDescriptor, index: Int, value: Short): Unit =
            put(descriptor, index, value)

        override fun encodeCharElement(descriptor: SerialDescriptor, index: Int, value: Char): Unit =
            put(descriptor, index, value)

        override fun encodeIntElement(descriptor: SerialDescriptor, index: Int, value: Int): Unit =
            put(descriptor, index, value)

        override fun encodeLongElement(descriptor: SerialDescriptor, index: Int, value: Long): Unit =
            put(descriptor, index, value)

        override fun encodeFloatElement(descriptor: SerialDescriptor, index: Int, value: Float): Unit =
            put(descriptor, index, value)

        override fun encodeDoubleElement(descriptor: SerialDescriptor, index: Int, value: Double): Unit =
            put(descriptor, index, value)

        override fun encodeStringElement(descriptor: SerialDescriptor, index: Int, value: String): Unit =
            put(descriptor, index, value)

        override fun encodeInlineElement(descriptor: SerialDescriptor, index: Int): Encoder =
            this@AnyTreeEncoder

        override fun <T : Any?> encodeSerializableElement(
            descriptor: SerialDescriptor,
            index: Int,
            serializer: SerializationStrategy<T>,
            value: T
        ) = put(descriptor, index, AnyTreeEncoder().apply {
            serializer.serialize(this, value)
        }.value)

        override fun <T : Any> encodeNullableSerializableElement(
            descriptor: SerialDescriptor,
            index: Int,
            serializer: SerializationStrategy<T>,
            value: T?
        ): Unit = value?.let { value ->
            encodeSerializableElement(descriptor, index, serializer, value)
        } ?: put(descriptor, index, null)

        private fun put(descriptor: SerialDescriptor, index: Int, value: Any?) {
            map.put(descriptor.getElementName(index), value)
        }
    }
}

public fun <T> SerializationStrategy<T>.encodeToAny(value: T): Any? = AnyTreeEncoder().apply {
    serialize(this, value)
}.value