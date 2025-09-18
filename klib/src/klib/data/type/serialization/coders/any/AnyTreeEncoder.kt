package klib.data.type.serialization.coders.any

import klib.data.type.serialization.serializers.any.NullableAnySerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.serializerOrNull

public class AnyTreeEncoder(
    private val encoder: Encoder,
) : Encoder by encoder {

    private val listSerializer = ListSerializer(NullableAnySerializer)

    private val mapSerializer = MapSerializer(NullableAnySerializer, NullableAnySerializer)

    @Suppress("UNCHECKED_CAST")
    public fun encodeNullableValue(value: Any?) {
        if (value == null)
            return encodeNull()

        (value::class.serializerOrNull() as KSerializer<Any?>?)?.let { serializer ->
            return encodeSerializableValue(serializer, value)
        }

        when (value) {
            is List<*> -> encodeSerializableValue(listSerializer, value)

            is Map<*, *> -> encodeSerializableValue(mapSerializer, value as Map<Any?, Any?>)

            else -> throw SerializationException("Unsupported value: ${value::class.simpleName}")
        }
    }
}

public val Encoder.asAnyTreeEncoder: AnyTreeEncoder
    get() = this as? AnyTreeEncoder ?: AnyTreeEncoder(this)
