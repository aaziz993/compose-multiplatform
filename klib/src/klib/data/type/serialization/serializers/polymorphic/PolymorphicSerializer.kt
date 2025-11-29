@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.type.serialization.serializers.polymorphic

import klib.data.type.serialization.subclasses
import kotlin.reflect.KClass
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
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
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

public open class PolymorphicSerializer<T : Any>(
    override val baseClass: KClass<T>,
    baseSerializer: KSerializer<T>? = null,
    public val subclasses: Map<KClass<out T>, KSerializer<out T>>,
    classDiscriminator: String = "type",
    valueDiscriminator: String = "value"
) : AbstractPolymorphicSerializer<T>() {

    public val serializersModule: SerializersModule = SerializersModule {

        polymorphic(baseClass, baseSerializer) {
            subclasses(subclasses)
        }
    }

    public override val descriptor: SerialDescriptor by lazy(LazyThreadSafetyMode.PUBLICATION) {
        buildSerialDescriptor("klib.data.type.serialization.serializers.polymorphic.Polymorphic", PolymorphicKind.OPEN) {
            element(classDiscriminator, String.serializer().descriptor)
            element(
                valueDiscriminator,
                buildSerialDescriptor(
                    "klib.data.type.serialization.serializers.polymorphic.Polymorphic<${baseClass.simpleName}>",
                    SerialKind.CONTEXTUAL,
                ),
            )
        }.withContext(baseClass)
    }

    override fun findPolymorphicSerializerOrNull(encoder: Encoder, value: T): SerializationStrategy<T>? =
        super.findPolymorphicSerializerOrNull(encoder, value)
            ?: serializersModule.getPolymorphic(baseClass, value)

    override fun findPolymorphicSerializerOrNull(
        decoder: CompositeDecoder,
        klassName: String?
    ): DeserializationStrategy<T>? =
        super.findPolymorphicSerializerOrNull(decoder, klassName)
            ?: serializersModule.getPolymorphic(baseClass, klassName)
}
