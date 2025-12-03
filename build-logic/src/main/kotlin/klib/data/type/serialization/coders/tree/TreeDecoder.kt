@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.type.serialization.coders.tree

import klib.data.type.cast
import klib.data.type.collections.entries
import klib.data.type.collections.list.asNullableList
import klib.data.type.collections.list.asNullableListOrNull
import klib.data.type.collections.map.asMap
import klib.data.type.collections.map.asMapOrNull
import klib.data.type.collections.map.toSortedValues
import klib.data.type.primitives.number.toInt
import klib.data.type.primitives.number.toNumber
import klib.data.type.serialization.annotations.SerialContext
import klib.data.type.serialization.annotations.SerialScript
import klib.data.type.serialization.annotations.TypeSerial
import klib.data.type.serialization.coders.AbstractDecoder
import klib.data.type.serialization.coders.any.AnyDecoder
import klib.data.type.serialization.coders.model.TreeDecoderConfiguration
import klib.data.type.serialization.getElementAnnotation
import klib.data.type.serialization.hasElementAnnotation
import klib.data.type.serialization.toPolymorphicValues
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.elementNames
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.decodeIfNullable
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer
import kotlinx.serialization.serializerOrNull

public open class TreeDecoder(
    protected val value: Any?,
    override val serializersModule: SerializersModule = EmptySerializersModule(),
    public val configuration: TreeDecoderConfiguration = TreeDecoderConfiguration()
) : AbstractDecoder() {

    protected lateinit var serializer: KSerializer<*>

    public open fun decodeNullableValue(): Any? = value

    protected open fun decodeSerializableValueMark(): Boolean = true

    final override fun decodeValue(): Any = decodeNullableValue()!!
    override fun decodeNotNullMark(): Boolean = configuration.decodeNotNullMark(value)

    final override fun decodeByte(): Byte = decodeNumber()

    final override fun decodeShort(): Short = decodeNumber()

    final override fun decodeInt(): Int = decodeNumber()

    final override fun decodeLong(): Long = decodeNumber()

    final override fun decodeFloat(): Float = decodeNumber()

    final override fun decodeDouble(): Double = decodeNumber()

    private inline fun <reified T : Comparable<T>> decodeNumber(): T = decodeValue().toNumber()

    final override fun decodeChar(): Char = decodeValue().toString().single()

    final override fun decodeString(): String = decodeValue().toString()

    final override fun decodeEnum(enumDescriptor: SerialDescriptor): Int =
        decodeValue().toString().let { value ->
            if (configuration.decodeEnumsCaseInsensitive) enumDescriptor.elementNames.indexOfFirst { elementName ->
                elementName.equals(value, true)
            }
            else enumDescriptor.getElementIndex(value)
        }

    @Suppress("UNCHECKED_CAST")
    final override fun <T> decodeSerializableValue(deserializer: DeserializationStrategy<T>): T {
        serializer = deserializer as KSerializer<T>
        return if (decodeSerializableValueMark()) super.decodeSerializableValue(deserializer)
        else decodeNullableValue() as T
    }

    final override fun <T : Any> decodeNullableSerializableValue(deserializer: DeserializationStrategy<T?>): T? {
        serializer = deserializer as KSerializer<T?>
        return decodeIfNullable(deserializer) {
            decodeSerializableValue(deserializer)
        }
    }

    final override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder =
        when (descriptor.kind) {
            // Array, List, Set
            StructureKind.LIST -> ListDecoder(descriptor)

            StructureKind.MAP -> MapDecoder(descriptor)

            // Object, Class, Polymorphic
            else -> StructureDecoder(descriptor)
        }

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int = throw NotImplementedError()

    public abstract inner class StructureLikeDecoder(protected val descriptor: SerialDescriptor) : TreeDecoder(
        decodeValue(),
        serializersModule,
        configuration,
    ) {

        protected abstract val values: List<Any?>

        protected var index: Int = -1
            private set

        public var consumeUnknownKeys: (SerialDescriptor, unknownKeys: Any?) -> Boolean = { _, _ -> false }

        override fun decodeNullableValue(): Any? = values[index].let { value ->
            if (decodeComputeValueMark() && value is String)
                configuration.computeValue(descriptor, decodeElementIndex(), value)
            else value
        }

        override fun decodeSerializableValueMark(): Boolean = !decodeComputeValueMark() &&
            configuration.serializableValueMark(descriptor, decodeElementIndex())

        private fun decodeComputeValueMark(): Boolean =
            descriptor.hasElementAnnotation<SerialScript>(decodeElementIndex()) ||
                descriptor.hasElementAnnotation<SerialScript>(decodeElementIndex())

        final override fun decodeNotNullMark(): Boolean = configuration.decodeNotNullMark(decodeNullableValue())

        final override fun decodeElementIndex(descriptor: SerialDescriptor): Int =
            if (++index < values.size) decodeElementIndex() else CompositeDecoder.DECODE_DONE

        protected open fun decodeElementIndex(): Int = index

        final override fun decodeCollectionSize(descriptor: SerialDescriptor): Int = values.size

        final override fun <T : Any> decodeNullableSerializableElement(
            descriptor: SerialDescriptor,
            index: Int,
            deserializer: DeserializationStrategy<T?>,
            previousValue: T?
        ): T? = decodeNullableSerializableValue(deserializer)

        override fun endStructure(descriptor: SerialDescriptor) {
            decodeUnknownKeys()?.let { unknownKeys ->
                if (!consumeUnknownKeys(descriptor, unknownKeys) && this@TreeDecoder is StructureLikeDecoder)
                    this@TreeDecoder.setChildUnknownKeys(unknownKeys)
            }
        }

        public abstract fun decodeUnknownKeys(): Any?

        public abstract fun setChildUnknownKeys(unknownKeys: Any?)
    }

    private inner class ListDecoder(descriptor: SerialDescriptor) : StructureLikeDecoder(descriptor) {

        override val values: List<Any?> =
            (value?.asNullableListOrNull ?: value!!.asMap<Any?, Any?>().toSortedValues { (key, _) -> key!!.toInt() })
                .withIndex()
                .filter { (index, element) -> configuration.filterElement(descriptor, index, element) }
                .map { (index, element) ->
                    configuration.transformElement(descriptor, index, element)
                }

        private var unknownKeys = listOf<Any?>()

        override fun decodeUnknownKeys(): Any? = unknownKeys.ifEmpty { null }

        override fun setChildUnknownKeys(unknownKeys: Any?) {
            this.unknownKeys += unknownKeys
        }
    }

    private inner class MapDecoder(descriptor: SerialDescriptor) : StructureLikeDecoder(descriptor) {

        override val values: List<Any?> = (value?.asMapOrNull<Any?, Any?>()?.entries
            ?: value!!.asNullableList.entries)
            .flatMapIndexed { index, entry ->
                configuration.transformEntry(descriptor, index, entry)
                    ?.let { (key, value) -> listOf(key, value) }.orEmpty()
            }

        private var unknownKeys = listOf<Any?>()

        override fun decodeUnknownKeys(): Any? =
            unknownKeys.chunked(2).associate { (key, value) -> key to value }.ifEmpty { null }

        override fun setChildUnknownKeys(unknownKeys: Any?) {
            this.unknownKeys += unknownKeys
        }
    }

    public inner class StructureDecoder(descriptor: SerialDescriptor) :
        StructureLikeDecoder(descriptor) {

        @Suppress("UNCHECKED_CAST")
        private var unknownKeys = value as Map<Any?, Any?>

        override val values: List<IndexedValue<Any?>> = when (descriptor.kind) {
            StructureKind.OBJECT -> emptyList()

            StructureKind.CLASS ->
                (0 until descriptor.elementsCount).mapNotNull { index ->
                    descriptor.getElementAnnotation<TypeSerial>(index)?.let { annotation ->
                        return@mapNotNull IndexedValue(
                            index,
                            configuration.typeParameterSerializer(this@TreeDecoder.serializer, annotation.index),
                        )
                    }

                    if (descriptor.hasElementAnnotation<SerialContext>(index))
                        return@mapNotNull configuration.serializationContext(descriptor, index)?.let { serialContext ->
                            IndexedValue(index, serialContext)
                        }

                    configuration.transformProperty(descriptor, index, unknownKeys)?.let { (key, value) ->
                        unknownKeys -= key
                        IndexedValue(index, value)
                    }
                }.also {
                    if (unknownKeys.isNotEmpty() && !configuration.ignoreUnknownKeys(descriptor))
                        throw SerializationException(
                            "Encountered an unknown keys '$unknownKeys' \n$IGNORE_UNKNOWN_KEYS_HINT",
                        )
                }

            is PolymorphicKind -> descriptor.toPolymorphicValues(
                value.cast(),
                configuration.classDiscriminator(descriptor),
            ).withIndex().toList()

            else -> throw IllegalArgumentException("Unknown descriptor: $descriptor")
        }

        override fun decodeSerializableValueMark(): Boolean = !decodeTypeSerialMark()

        private fun decodeTypeSerialMark(): Boolean =
            descriptor.hasElementAnnotation<TypeSerial>(decodeElementIndex())

        override fun decodeNullableValue(): Any? = values[index].value

        override fun decodeElementIndex(): Int = values[index].index

        override fun decodeUnknownKeys(): Any? = unknownKeys.ifEmpty { null }

        override fun setChildUnknownKeys(unknownKeys: Any?): Unit =
            if (descriptor.kind is PolymorphicKind)
                (this@TreeDecoder as StructureLikeDecoder).setChildUnknownKeys(unknownKeys)
            else this.unknownKeys += descriptor.getElementName(decodeElementIndex()) to unknownKeys
    }

    public companion object {

        private const val IGNORE_UNKNOWN_KEYS_HINT =
            "Use 'ignoreUnknownKeys = { true }' to ignore unknown keys."
    }
}

internal class TreeAnyDecoder(decoder: TreeDecoder) : AnyDecoder<TreeDecoder>(decoder) {

    override fun decodeValue(): Any? = decoder.decodeNullableValue()

    override fun <T> decodeSerializableValue(deserializer: DeserializationStrategy<T>, value: Any?): T =
        deserializer.deserialize(value, decoder.serializersModule, decoder.configuration)
}

@Suppress("UNCHECKED_CAST")
public fun <T> DeserializationStrategy<T>.deserialize(
    value: Any?,
    serializersModule: SerializersModule = EmptySerializersModule(),
    configuration: TreeDecoderConfiguration = TreeDecoderConfiguration()
): T = TreeDecoder(value, serializersModule, configuration).decodeSerializableValue(this)

public inline fun <reified T : Any> Any?.deserialize(
    serializersModule: SerializersModule = EmptySerializersModule(),
    configuration: TreeDecoderConfiguration = TreeDecoderConfiguration()
): T = (T::class.serializerOrNull() ?: serializersModule.serializer())
    .deserialize(this, serializersModule, configuration)
