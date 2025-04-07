@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.type.serialization.json.serializer

import kotlin.collections.find
import kotlin.jvm.kotlin
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.SealedClassSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.PolymorphicKind.SEALED
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.asJsonDecoder
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.SerializersModuleBuilder
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.serializer
import kotlinx.serialization.serializerOrNull
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder

public open class JsonContentPolymorphicSerializer<T : Any>(
    private val baseClass: KClass<T>,
    private val subclasses: List<KClass<out T>>? = null,
) : JsonContentPolymorphicSerializer<T>(baseClass) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<T> {

        return (if (baseClass.isSealed) baseClass.sealedSubclasses else subclasses)?.find { clazz ->
            clazz.serializer().descriptor.serialName == serialName
        }?.serializer() ?: throw SerializationException("Polymorphic serializer not found for: $element")
    }
}

private val reflections = Reflections(
    ConfigurationBuilder()
        .setUrls(ClasspathHelper.forPackage(""))
        .setScanners(Scanners.SubTypes),
)

public fun <T : Any> KClass<out T>.getSerializableSubclasses(): List<Class<out T>> =
    reflections.getSubTypesOf(java)
        .filter { it.kotlin.hasAnnotation<Serializable>() }
