@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.type.serialization.serializers.transform

import com.charleskorn.kaml.YamlContentPolymorphicSerializer
import klib.data.type.serialization.coders.any.asAnyDecoder
import klib.data.type.serialization.getPolymorphicOrValue
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.reflect.KClass

/**
 * Base class for custom serializers that allows selecting polymorphic serializer
 * without a dedicated class discriminator, on a content basis.
 *
 * Usually, polymorphic serialization (represented by [klib.data.type.serialization.serializers.polymorphic.PolymorphicSerializer] and [SealedClassSerializer])
 * requires a dedicated `"type"` property in the Any? to
 * determine actual serializer that is used to deserialize Kotlin class.
 *
 * However, sometimes (e.g. when interacting with external API) type property is not present in the input
 * and it is expected to guess the actual type by the shape of Any?, for example by the presence of specific key.
 * [ContentPolymorphicSerializer] provides a skeleton implementation for such strategy. Please note that
 *
 * Deserialization happens in two stages: first, a value from the input is read
 * to as a [Any?]. Second, [selectDeserializer] function is called to determine which serializer should be used.
 * The returned serializer is used to deserialize [Any?] back to Kotlin object.
 *
 * It is possible to serialize values this serializer. In that case, class discriminator property won't
 * be added to stream, i.e., deserializing a class from the string and serializing it back yields the original string.
 * However, to determine a serializer, a standard polymorphic mechanism represented by [SerializersModule] is used.
 * For convenience, [serialize] method can lookup default serializer, but it is recommended to follow
 * standard procedure with [registering][SerializersModuleBuilder.polymorphic].
 *
 * Usage example:
 * ```
 * interface Payment {
 *     val amount: String
 * }
 *
 * @Serializable
 * data class SuccessfulPayment(override val amount: String, val date: String) : Payment
 *
 * @Serializable
 * data class RefundedPayment(override val amount: String, val date: String, val reason: String) : Payment
 *
 * object PaymentSerializer : ContentPolymorphicSerializer<Payment>(Payment::class) {
 *     override fun selectDeserializer(content: Any?) = when {
 *         "reason" in content.asNullableMap -> RefundedPayment.serializer()
 *         else -> SuccessfulPayment.serializer()
 *     }
 * }
 *
 * // Now both statements will yield different subclasses of Payment:
 *
 * ?.decodeFromString(PaymentSerializer, """{"amount":"1.0","date":"03.02.2020"}""")
 * ?.decodeFromString(PaymentSerializer, """{"amount":"2.0","date":"03.02.2020","reason":"complaint"}""")
 * ```
 *
 * @param T A root type for all classes that could be possibly encountered during serialization and deserialization.
 * Must be non-final class or interface.
 * @param baseClass A class token for [T].
 */
public abstract class ContentPolymorphicSerializer<T : Any>(public val baseClass: KClass<T>) :
    KSerializer<T> {

    /**
     * A descriptor for this set of content-based serializers.
     * By default, it uses the name composed of [baseClass] simple name,
     * kind is set to [PolymorphicKind.SEALED] and contains 0 elements.
     *
     * However, this descriptor can be overridden to achieve better representation of custom transformed Any? shape
     * for schema generating/introspection purposes.
     */
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor(
            "ContentPolymorphicSerializer<${baseClass.simpleName}>",
            PolymorphicKind.SEALED,
        ) {
            // To support yaml
            annotations += YamlContentPolymorphicSerializer.Marker()
        }

    final override fun serialize(encoder: Encoder, value: T): Unit =
        encoder.serializersModule.getPolymorphicOrValue(baseClass, value).serialize(encoder, value)

    final override fun deserialize(decoder: Decoder): T {
        val anyDecoder = decoder.asAnyDecoder

        val decoded = anyDecoder.decodeValue()

        val tSerializer = selectDeserializer(decoded)

        return anyDecoder.decodeSerializableValue(tSerializer, decoded)
    }

    /**
     * Determines a particular strategy for deserialization by looking on a parsed Any? [value].
     */
    protected abstract fun selectDeserializer(value: Any?): DeserializationStrategy<T>
}
