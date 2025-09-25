package klib.data.type.serialization.coders

import klib.data.type.serialization.buildPolymorphicDescriptor
import klib.data.type.serialization.classDiscriminator
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.findPolymorphicSerializer
import kotlinx.serialization.internal.AbstractPolymorphicSerializer

@Suppress("UNCHECKED_CAST")
public fun <T> Encoder.encodePolymorphically(
    serializer: SerializationStrategy<T>,
    value: T,
    classDiscriminator: (SerialDescriptor) -> String,
): Boolean {
    if (serializer is AbstractPolymorphicSerializer<*>)
        classDiscriminator(serializer.descriptor)
            .takeIf { discriminator -> discriminator != serializer.descriptor.classDiscriminator }
            ?.let { discriminator ->
                val casted = serializer as AbstractPolymorphicSerializer<Any>
                requireNotNull(value) { "Value for serializer ${serializer.descriptor} should always be non-null." }
                val actualSerializer =
                    casted.findPolymorphicSerializer(this, value) as SerializationStrategy<T>

                val descriptor = serializer.descriptor.buildPolymorphicDescriptor(discriminator)

                encodeStructure(descriptor) {
                    encodeStringElement(descriptor, 0, actualSerializer.descriptor.serialName)
                    encodeSerializableElement(descriptor, 1, actualSerializer, value)
                }
                return true
            }
    return false
}

@PublishedApi
internal class MapCompositeEncoder(
    private val encoder: Encoder,
    private val compositeEncoder: CompositeEncoder
) : Encoder by encoder {

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder =
        object : CompositeEncoder by compositeEncoder {
            // Prevents end of encoding
            override fun endStructure(descriptor: SerialDescriptor) {}
        }
}

public inline fun Encoder.beginStructure(
    descriptor: SerialDescriptor,
    crossinline block: Encoder.() -> Unit,
): CompositeEncoder = beginStructure(descriptor).also { compositeEncoder ->
    MapCompositeEncoder(this, compositeEncoder).block()
}

/**
 * Begins a structure, encodes it using the given [structureBlock] with additional encoding and ends it.
 */
public inline fun Encoder.encodeStructure(
    descriptor: SerialDescriptor,
    crossinline structureBlock: Encoder.() -> Unit,
    crossinline block: CompositeEncoder.() -> Unit
) {
    val composite = beginStructure(descriptor, structureBlock)
    composite.block()
    composite.endStructure(descriptor)
}
