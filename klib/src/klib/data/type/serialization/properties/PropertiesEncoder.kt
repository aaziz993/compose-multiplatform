package klib.data.type.serialization.properties

import klib.data.type.primitives.string.addSuffixIfNotEmpty
import klib.data.type.serialization.coders.encodePolymorphically
import klib.data.type.serialization.coders.encodeStructure
import klib.data.type.serialization.properties.annotations.PropertiesComment
import klib.data.type.serialization.serializers.any.NullableAnySerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.modules.SerializersModule


public open class PropertiesEncoder(
    private val writer: PropertiesWriter,
    public val properties: Properties,
) : AbstractEncoder() {

    override val serializersModule: SerializersModule = properties.serializersModule

    protected var key: String = ""

    private val transformName: (SerialDescriptor, Int) -> String =
        properties.configuration.namingStrategy?.let { namingStrategy ->
            { descriptor, index ->
                namingStrategy.serialNameForProperties(descriptor, index)
            }
        } ?: { descriptor, index -> descriptor.getElementName(index) }

    protected open fun encodeValue(value: String): Unit = writer.writeValue(value)

    override fun encodeValue(value: Any): Unit = encodeValue(value.toString())

    override fun encodeNull(): Unit = encodeValue("")

    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int): Unit =
        encodeValue(enumDescriptor.getElementName(index))

    override fun <T> encodeSerializableValue(serializer: SerializationStrategy<T>, value: T) {
        if (!encodePolymorphically(serializer, value) { properties.configuration.classDiscriminator })
            super.encodeSerializableValue(serializer, value)
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
        val parentKey = key.addSuffixIfNotEmpty(properties.configuration.keySeparator)

        return when (descriptor.kind) {
            StructureKind.LIST -> ListEncoder(descriptor, parentKey)

            StructureKind.MAP -> MapEncoder(descriptor, parentKey)

            else -> StructureEncoder(descriptor, parentKey)
        }
    }

    override fun shouldEncodeElementDefault(descriptor: SerialDescriptor, index: Int): Boolean =
        properties.configuration.encodeDefaults

    private open inner class StructureEncoder(
        private val descriptor: SerialDescriptor,
        protected val parentKey: String
    ) : PropertiesEncoder(writer, properties) {
        protected var index: Int = 0

        override fun encodeElement(descriptor: SerialDescriptor, index: Int): Boolean {
            this.index = index
            key = "$parentKey${transformName(descriptor, index)}"
            return super.encodeElement(descriptor, index)
        }

        override fun encodeValue(value: String) {
            descriptor.getElementAnnotations(index)
                .filterIsInstance<PropertiesComment>()
                .flatMap { annotation -> annotation.lines.toList() }
                .forEach(writer::writeComment)

            writer.writeKeyValue(key, value)
        }
    }

    private inner class ListEncoder(descriptor: SerialDescriptor, parentKey: String) :
        StructureEncoder(descriptor, parentKey) {

        override fun encodeElement(descriptor: SerialDescriptor, index: Int): Boolean {
            this.index = index
            key = "$parentKey$index"
            return true
        }

        override fun encodeValue(value: String) {
            writer.writeKey(key)
            writer.writeValue(value)
        }
    }

    private inner class MapEncoder(descriptor: SerialDescriptor, parentKey: String) :
        StructureEncoder(descriptor, parentKey) {

        override fun encodeElement(descriptor: SerialDescriptor, index: Int): Boolean {
            this.index = index
            return true
        }

        override fun encodeValue(value: String) {
            if (index % 2 == 0) key = "$parentKey$value"
            else writer.writeKeyValue(key, value)
        }
    }
}

public fun PropertiesEncoder.encodeAny(value: Map<String, Any?>): Unit =
    encodeSerializableValue(NullableAnySerializer, value)