@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.type.serialization.encoder


import klib.data.type.serialization.serializer.OptionalAnySerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeCollection
import kotlinx.serialization.internal.ArrayListClassDesc
import kotlinx.serialization.internal.LinkedHashMapClassDesc


@Suppress("UNCHECKED_CAST")
private class TreeEncoder(
    private val encoder: Encoder,
) : Encoder by encoder {

    private val optionalAnyDescriptor = OptionalAnySerializer.descriptor

    private val listDescriptor = ArrayListClassDesc(optionalAnyDescriptor)

    private val mapDescriptor = LinkedHashMapClassDesc(
        String.serializer().descriptor,
        optionalAnyDescriptor
    )

    fun encodeAny(value: Any?) {
        when (value) {
            null -> encodeNull()
            is Boolean -> encodeBoolean(value)
            is Byte -> encodeByte(value)
            is Short -> encodeShort(value)
            is Char -> encodeChar(value)
            is Int -> encodeInt(value)
            is Long -> encodeLong(value)
            is Float -> encodeFloat(value)
            is Double -> encodeDouble(value)
            is String -> encodeString(value)

            is List<*> -> encodeCollection(
                listDescriptor,
                value.size
            ) {
                value.forEachIndexed { index, element ->
                    encodeSerializableElement(
                        listDescriptor,
                        index,
                        OptionalAnySerializer,
                        element
                    )
                }
            }

            is Map<*, *> -> encodeCollection(
                mapDescriptor,
                value.size
            ) {
                value.entries.forEachIndexed { index, (key, value) ->
                    val keyIndex = index * 2
                    encodeSerializableElement(mapDescriptor, keyIndex, OptionalAnySerializer, key)
                    encodeSerializableElement(mapDescriptor, keyIndex + 1, OptionalAnySerializer, value)
                }
            }
        }
    }
}

public fun Encoder.encodeAny(value: Any?) = TreeEncoder(this).encodeAny(value)