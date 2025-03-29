package gradle.serialization.serializer

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

    public companion object {

        @Suppress("UNCHECKED_CAST")
        private fun <T : Any> KClass<out T>.getPolymorphicSerializer(type: String): KSerializer<T>? =
            Reflections().getSubTypesOf(java)
                .filter { it.kotlin.hasAnnotation<Serializable>() }
                .find { clazz ->
                    (clazz.getAnnotation(SerialName::class.java)?.value ?: clazz.simpleName) == type
                }?.kotlin?.serializer() as KSerializer<T>?
    }
}
