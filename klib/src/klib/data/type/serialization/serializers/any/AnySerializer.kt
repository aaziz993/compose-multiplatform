package klib.data.type.serialization.serializers.any

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

public object AnySerializer : KSerializer<Any> {

    override val descriptor: SerialDescriptor = buildSerialDescriptor("Any", SerialKind.CONTEXTUAL)

    override fun serialize(encoder: Encoder, value: Any): Unit = NullableAnySerializer.serialize(encoder, value)

    override fun deserialize(decoder: Decoder): Any = NullableAnySerializer.deserialize(decoder)!!
}

public typealias SerializableAny = @Serializable(with = AnySerializer::class) Any
