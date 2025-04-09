@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE", "BOUNDS_NOT_ALLOWED_IF_BOUNDED_BY_TYPE_PARAMETER")

package klib.data.type.serialization.serializer

import klib.data.type.asMap
import klib.data.type.collection.slice
import klib.data.type.serialization.encoder.decodeAny
import klib.data.type.serialization.encoder.deserialize
import klib.data.type.serialization.encoder.encodeAny
import klib.data.type.serialization.encoder.serialize
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.elementNames
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

public open class MapSerializer<V, C : Map<String, V>, B>(
    private val tSerializer: KSerializer<B>,
    private val valueSerializer: KSerializer<V>,
) : KSerializer<C> where B : C, B : MutableMap<String, V> {

    override val descriptor: SerialDescriptor = tSerializer.descriptor

    @Suppress("UNCHECKED_CAST")
    override fun serialize(encoder: Encoder, value: C) =
        encoder.encodeAny(
            tSerializer.serialize(value as B).asMap
                    + value.mapValues { (_, value) -> valueSerializer.serialize(value) }
        )

    override fun deserialize(decoder: Decoder): C =
        decoder.decodeAny().asMap.let { map ->
            tSerializer.deserialize(map.slice(descriptor.elementNames)).apply {
                putAll((map - descriptor.elementNames).mapValues { (_, value) -> valueSerializer.deserialize(value) })
            }
        }
}