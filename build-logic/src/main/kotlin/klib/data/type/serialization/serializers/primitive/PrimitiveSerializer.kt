package klib.data.type.serialization.serializers.primitive

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

public abstract class PrimitiveSerializer<T : Any>(
    serialName: String,
    kind: PrimitiveKind,
    public val serializer: (Encoder, T) -> Unit,
    public val deserializer: (Decoder) -> T,
) : KSerializer<T> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(serialName, kind)

    override fun serialize(encoder: Encoder, value: T): Unit = serializer(encoder, value)

    override fun deserialize(decoder: Decoder): T = deserializer(decoder)
}
