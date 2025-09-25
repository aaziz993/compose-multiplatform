package klib.data.type.serialization.serializers.primitive

import kotlin.reflect.KClass
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.subclass
import kotlinx.serialization.serializer

public class PolymorphicPrimitiveSerializer<T : Any>(public val tSerializer: KSerializer<T>) : KSerializer<T> {

    public constructor(kClass: KClass<T>) : this(kClass.serializer())

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor(tSerializer.descriptor.serialName) {
            element("value", tSerializer.descriptor)
        }

    override fun serialize(encoder: Encoder, value: T): Unit = encoder.encodeStructure(descriptor) {
        encodeSerializableElement(descriptor, 0, tSerializer, value)
    }

    override fun deserialize(decoder: Decoder): T = decoder.decodeStructure(descriptor) {
        decodeSerializableElement(descriptor, decodeElementIndex(descriptor), tSerializer)
    }
}

@Suppress("UNCHECKED_CAST")
public inline fun <reified T : Any> PolymorphicModuleBuilder<T>.primitive(serializer: KSerializer<T>): Unit =
    subclass(PolymorphicPrimitiveSerializer(serializer))

@Suppress("UNCHECKED_CAST")
public inline fun <reified T : Any> PolymorphicModuleBuilder<T>.primitive(kClass: KClass<T>): Unit =
    primitive(kClass.serializer())
