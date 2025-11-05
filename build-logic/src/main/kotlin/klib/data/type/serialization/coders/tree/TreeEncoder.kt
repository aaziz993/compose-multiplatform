package klib.data.type.serialization.coders.tree

import klib.data.type.collections.map.with
import klib.data.type.serialization.annotations.SerialContext
import klib.data.type.serialization.annotations.TypeSerial
import klib.data.type.serialization.coders.any.AnyEncoder
import klib.data.type.serialization.coders.any.asAnyTreeEncoder
import klib.data.type.serialization.coders.encodePolymorphically
import klib.data.type.serialization.coders.model.TreeEncoderConfiguration
import klib.data.type.serialization.getElementAnnotation
import klib.data.type.serialization.hasElementAnnotation
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer

public open class TreeEncoder(
    override val serializersModule: SerializersModule = EmptySerializersModule(),
    public val configuration: TreeEncoderConfiguration = TreeEncoderConfiguration()
) : AbstractEncoder() {

    public open var value: Any? = null
        protected set

    protected open fun encodeNullableValue(value: Any?) {
        this.value = value
    }

    protected open fun encodeSerializableValueMark(): Boolean = true

    final override fun encodeValue(value: Any): Unit = encodeNullableValue(value)

    final override fun encodeNull(): Unit = encodeNullableValue(null)

    final override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int): Unit =
        encodeValue(enumDescriptor.getElementName(index))

    final override fun <T> encodeSerializableValue(serializer: SerializationStrategy<T>, value: T) {
        if (encodeSerializableValueMark()) {
            if (!encodePolymorphically(serializer, value, configuration.classDiscriminator))
                super.encodeSerializableValue(serializer, value)
        }
        else encodeNullableValue(value)
    }

    final override fun beginCollection(descriptor: SerialDescriptor, collectionSize: Int): CompositeEncoder =
        when (descriptor.kind) {
            StructureKind.LIST -> ListEncoder(descriptor, collectionSize)
            else -> MapEncoder(descriptor, collectionSize)
        }

    final override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder =
        StructureEncoder(descriptor)

    final override fun shouldEncodeElementDefault(descriptor: SerialDescriptor, index: Int): Boolean =
        configuration.encodeDefaults

    private abstract inner class StructureLikeEncoder(protected val descriptor: SerialDescriptor, collectionSize: Int) :
        TreeEncoder(serializersModule, configuration) {

        protected val values: ArrayList<Any?> = ArrayList(collectionSize)

        protected var index = 0

        override fun encodeSerializableValueMark(): Boolean = configuration.serializableValueMark(descriptor, index)

        final override fun endStructure(descriptor: SerialDescriptor) =
            this@TreeEncoder.encodeValue(value!!)

        override fun encodeElement(descriptor: SerialDescriptor, index: Int): Boolean {
            this.index = index
            return super.encodeElement(descriptor, index)
        }
    }

    private inner class ListEncoder(descriptor: SerialDescriptor, collectionSize: Int) : StructureLikeEncoder(
        descriptor,
        collectionSize,
    ) {

        override var value: Any? = null
            get() = values.apply(ArrayList<*>::trimToSize)

        override fun encodeNullableValue(value: Any?) {
            if (configuration.filterElement(descriptor, index, value))
                values.add(configuration.transformElement(descriptor, index, value))
        }
    }

    private inner class MapEncoder(descriptor: SerialDescriptor, collectionSize: Int) : StructureLikeEncoder(
        descriptor,
        collectionSize * 2,
    ) {

        private var key: Any? = null

        override var value: Any? = null
            get() = values.apply(ArrayList<*>::trimToSize).chunked(2).associate { (key, value) ->
                key to value
            }

        override fun encodeNullableValue(value: Any?) {
            if (index % 2 == 0) key = value
            else configuration.transformEntry(descriptor, index / 2, key with value)?.let { (key, value) ->
                values.addAll(listOf(key, value))
            }
        }
    }

    private inner class StructureEncoder(descriptor: SerialDescriptor) : StructureLikeEncoder(
        descriptor,
        (0 until descriptor.elementsCount).count { index ->
            !(descriptor.hasElementAnnotation<TypeSerial>(index) ||
                descriptor.hasElementAnnotation<SerialContext>(index))
        } * 2,
    ) {

        override var value: Any? = null
            get() = values.apply(ArrayList<*>::trimToSize).chunked(2).associate { (key, value) ->
                key to value
            }

        override fun encodeElement(descriptor: SerialDescriptor, index: Int): Boolean {
            this.index = index
            return !(encodeTypeSerialMark() || encodeSerialContextMark()) && super.encodeElement(descriptor, index)
        }

        private fun encodeTypeSerialMark() = descriptor.hasElementAnnotation<TypeSerial>(index)

        private fun encodeSerialContextMark() =
            descriptor.getElementAnnotation<SerialContext>(index)?.serialize == false

        override fun encodeNullableValue(value: Any?) {
            configuration.transformProperty(descriptor, index, value)?.let { (name, value) ->
                values.addAll(listOf(name, value))
            }
        }
    }
}

internal class TreeAnyEncoder(encoder: TreeEncoder) : AnyEncoder<TreeEncoder>(encoder) {

    override fun <T> encodeSerializableValue(serializer: SerializationStrategy<T>, value: T): Any? {
        encoder.encodeNullableSerializableValue(serializer, value)
        return encoder.value
    }

    override fun encodeValue(value: Any?): Unit = encoder.asAnyTreeEncoder.encodeNullableValue(value)
}

public fun <T> SerializationStrategy<T>.serialize(
    value: T,
    serializersModule: SerializersModule = EmptySerializersModule(),
    configuration: TreeEncoderConfiguration = TreeEncoderConfiguration(),
): Any? = TreeEncoder(serializersModule, configuration).apply {
    encodeSerializableValue(this@serialize, value)
}.value

public inline fun <reified T : Any> T.serialize(
    serializersModule: SerializersModule = EmptySerializersModule(),
    configuration: TreeEncoderConfiguration = TreeEncoderConfiguration(),
): Any? = serializer<T>().serialize(this, serializersModule, configuration)
