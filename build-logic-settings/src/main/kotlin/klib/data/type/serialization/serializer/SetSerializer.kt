package klib.data.type.serialization.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


public open class SetSerializer<E, C : Set<E>>(
    eSerializer: KSerializer<E>,
    private val toResult: Set<E>.() -> C,
) : KSerializer<C> {
    private val delegate = SetSerializer(eSerializer)

    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun serialize(encoder: Encoder, value: C) {
        delegate.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): C = delegate.deserialize(decoder).toResult()
}
