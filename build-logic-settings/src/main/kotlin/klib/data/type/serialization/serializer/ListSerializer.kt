package klib.data.type.serialization.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


public open class ListSerializer<E, C : List<E>>(
    eSerializer: KSerializer<E>,
    private val toResult: List<E>.() -> C,
) : KSerializer<C> {
    private val delegate = ListSerializer(eSerializer)

    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun serialize(encoder: Encoder, value: C) {
        delegate.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): C = delegate.deserialize(decoder).toResult()
}
