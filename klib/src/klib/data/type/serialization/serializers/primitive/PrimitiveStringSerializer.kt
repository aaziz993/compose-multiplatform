package klib.data.type.serialization.serializers.primitive

import kotlinx.serialization.descriptors.PrimitiveKind

public abstract class PrimitiveStringSerializer<T : Any>(
    serialName: String,
    serializer: (T) -> String,
    deserializer: (String) -> T,
) : PrimitiveSerializer<T>(
    serialName,
    PrimitiveKind.STRING,
    { encoder, value ->
        encoder.encodeString(serializer(value))
    }, {
        deserializer(it.decodeString())
    })
