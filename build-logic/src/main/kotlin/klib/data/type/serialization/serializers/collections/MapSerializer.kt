@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE", "BOUNDS_NOT_ALLOWED_IF_BOUNDED_BY_TYPE_PARAMETER")

package klib.data.type.serialization.serializers.collections

import klib.data.type.serialization.buildMapClassSerialDescriptor
import klib.data.type.serialization.coders.any.asAnyDecoder
import klib.data.type.serialization.coders.encodeStructure
import klib.data.type.serialization.serializers.any.NullableAnySerializer
import klib.data.type.serialization.unknownKeysOf
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

public open class MapSerializer<C : Map<String, Any?>, B>(
    public val tSerializer: KSerializer<B>,
    public val unknownKeys: Boolean = true,
) : KSerializer<C> where B : C, B : MutableMap<String, Any?> {

    override val descriptor: SerialDescriptor = tSerializer.descriptor

    @Suppress("UNCHECKED_CAST")
    override fun serialize(encoder: Encoder, value: C) {
        // Create common descriptor for the class and map
        val descriptor = buildMapClassSerialDescriptor(
            tSerializer.descriptor,
            value.keys,
            NullableAnySerializer.descriptor,
        )

        encoder.encodeStructure(
            descriptor,
            {
                // Encode class properties
                tSerializer.serialize(this, value as B)
            },
        ) {
            // Encode map values
            value.values.forEachIndexed { index, value ->
                encodeNullableSerializableElement(
                    descriptor,
                    index + tSerializer.descriptor.elementsCount,
                    NullableAnySerializer,
                    value,
                )
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun deserialize(decoder: Decoder): C {
        val anyDecoder = decoder.asAnyDecoder

        val decoded = anyDecoder.decodeValue() as Map<String, Any?>

        return anyDecoder.decodeSerializableValue(tSerializer, decoded).apply {
            putAll(
                if (unknownKeys) descriptor.unknownKeysOf(decoded) else decoded,
            )
        }
    }
}



