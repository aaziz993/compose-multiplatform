package klib.data.type.serialization.json.serializer

import kotlin.jvm.kotlin
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.serializer
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder

public open class JsonContentPolymorphicSerializer<T : Any>(
    private val baseClass: KClass<T>,
    private val classDiscriminator: String = "type",
) : JsonContentPolymorphicSerializer<T>(baseClass) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<T> {
        val type = element.jsonObject[classDiscriminator]?.jsonPrimitive?.content
            ?: throw SerializationException("Class discriminator '$classDiscriminator' not found in: $element")
        return baseClass.getPolymorphicSerializer(type)
            ?: throw SerializationException("Polymorphic serializer not found for: $element")
    }
}

private val reflections = Reflections(
    ConfigurationBuilder()
        .setUrls(ClasspathHelper.forPackage(""))
        .setScanners(Scanners.SubTypes),
)

public val KClass<*>.polymorphicSerialNames: List<String>
    get() = getSerializable().map { clazz ->
        (clazz.getAnnotation(SerialName::class.java)?.value ?: clazz.simpleName)
    }

@Suppress("UNCHECKED_CAST")
public fun <T : Any> KClass<out T>.getPolymorphicSerializer(serialName: String): KSerializer<T>? =
    getSerializable()
        .find { clazz ->
            (clazz.getAnnotation(SerialName::class.java)?.value ?: clazz.simpleName) == serialName
        }?.kotlin?.serializer() as KSerializer<T>?

public fun <T : Any> KClass<out T>.getSerializable(): List<Class<out T>> =
    reflections.getSubTypesOf(java)
        .filter { it.kotlin.hasAnnotation<Serializable>() }
