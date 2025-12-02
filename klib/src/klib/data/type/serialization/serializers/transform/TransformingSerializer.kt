package klib.data.type.serialization.serializers.transform

import klib.data.type.serialization.coders.any.asAnyDecoder
import klib.data.type.serialization.coders.any.asAnyEncoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Base class for custom serializers that allows manipulating an abstract Any?
 * representation of the class before serialization or deserialization.
 *
 * [TransformingSerializer] provides capabilities to manipulate [Any?] representation
 * directly instead of interacting with [Encoder] and [Decoder] in order to apply a custom
 * transformation to the Any?.
 * Please note that this class expects that [Encoder] and [Decoder] are implemented by [Decoder] and [Encoder],
 * i.e. serializers derived from this class work only with [] format.
 *
 * There are two methods in which Any? transformation can be defined: [transformSerialize] and [transformDeserialize].
 * You can override one or both of them. Consult their documentation for details.
 *
 * Usage example:
 *
 * ```
 * @Serializable
 * data class Example(
 *     @Serializable(UnwrappingListSerializer::class) val data: String
 * )
 * // Unwraps a list to a single object
 * object UnwrappingListSerializer :
 *     TransformingSerializer<String>(String.serializer()) {
 *     override fun transformDeserialize(value: Any?): Any? {
 *         if (value !is List) return value
 *         require(value.size == 1) { "List size must be equal to 1 to unwrap it" }
 *         return value.first()
 *     }
 * }
 * // Now these functions both yield correct result:
 * .parse(Example.serializer(), """{"data":["str1"]}""")
 * .parse(Example.serializer(), """{"data":"str1"}""")
 * ```
 *
 * @param T A type for Kotlin property for which this serializer could be applied.
 *        **Not** the type that you may encounter in Any?. (e.g. if you unwrap a list
 *        to a single value `T`, use `T`, not `List<T>`)
 * @param tSerializer A serializer for type [T]. Determines [Any?] which is passed to [transformSerialize].
 *        Should be able to parse [Any?] from [transformDeserialize] function.
 *        Usually, default [serializer] is sufficient.
 */
public abstract class TransformingSerializer<T>(public val tSerializer: KSerializer<T>) : KSerializer<T> {

    /**
     * A descriptor for this transformation.
     * By default, it delegates to [tSerializer]'s descriptor.
     *
     * However, this descriptor can be overridden to achieve better representation of the resulting Any? shape
     * for schema generating or introspection purposes.
     */
    override val descriptor: SerialDescriptor = tSerializer.descriptor

    final override fun serialize(encoder: Encoder, value: T) {
        val anyEncoder = encoder.asAnyEncoder

        val encoded = anyEncoder.encodeSerializableValue(tSerializer, value)

        val transformed = transformSerialize(encoder, encoded)

        if (transformed === encoded) tSerializer.serialize(encoder, value)
        else anyEncoder.encodeValue(transformed)
    }

    final override fun deserialize(decoder: Decoder): T {
        val anyDecoder = decoder.asAnyDecoder

        val decoded = anyDecoder.decodeValue()

        val transformed = transformDeserialize(decoder, decoded)

        return anyDecoder.decodeSerializableValue(tSerializer, transformed)
    }

    /**
     * Transformation that happens during [serialize] call.
     * Does nothing by default.
     *
     * During serialization, a value of type [T] is serialized with [tSerializer] to a [Any?],
     * user transformation in [transformSerialize] is applied, and then resulting [Any?] is encoded to a Any? string.
     */
    protected open fun transformSerialize(encoder: Encoder, value: Any?): Any? = transformSerialize(value)

    protected open fun transformSerialize(value: Any?): Any? = value

    /**
     * Transformation that happens during [deserialize] call.
     * Does nothing by default.
     *
     * During deserialization, a value from Any? is firstly decoded to a [Any?],
     * user transformation in [transformDeserialize] is applied,
     * and then resulting [Any?] is deserialized to [T] with [tSerializer].
     */
    protected open fun transformDeserialize(decoder: Decoder, value: Any?): Any? = transformDeserialize(value)

    protected open fun transformDeserialize(value: Any?): Any? = value
}
