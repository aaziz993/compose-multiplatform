@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.type.serialization.serializer

import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.descriptors.withContext
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.internal.AbstractPolymorphicSerializer
import kotlinx.serialization.serializer

public open class PolymorphicSerializer<T : Any>(
    override val baseClass: KClass<T>,
    private val subclasses: List<KClass<out T>> = emptyList()
) : AbstractPolymorphicSerializer<T>() {

    public override val descriptor: SerialDescriptor by lazy(LazyThreadSafetyMode.PUBLICATION) {
        buildSerialDescriptor("data.type.serialization.Polymorphic", PolymorphicKind.OPEN) {
            element("type", String.serializer().descriptor)
            element(
                "value",
                buildSerialDescriptor(
                    "data.type.serialization.Polymorphic<${baseClass.simpleName}>",
                    SerialKind.CONTEXTUAL,
                ),
            )
        }.withContext(baseClass)
    }

    override fun findPolymorphicSerializerOrNull(encoder: Encoder, value: T): SerializationStrategy<T>? =
        super.findPolymorphicSerializerOrNull(encoder, value) ?: value::class.serializer() as KSerializer<T>

    override fun findPolymorphicSerializerOrNull(
        decoder: CompositeDecoder,
        klassName: String?
    ): DeserializationStrategy<T>? =
        super.findPolymorphicSerializerOrNull(decoder, klassName) ?: findFallbackSerializer(klassName)

    private fun findFallbackSerializer(klassName: String?) =
        (baseClass.sealedSubclasses + subclasses).filter { klass -> klass.hasAnnotation<Serializable>() }
            .let { kClasses ->
                (kClasses.find { klass ->
                    klass.findAnnotation<SerialName>()?.value == klassName
                } ?: kClasses.find { klass ->
                    klass.qualifiedName == klassName
                })?.serializer()
            }
}
