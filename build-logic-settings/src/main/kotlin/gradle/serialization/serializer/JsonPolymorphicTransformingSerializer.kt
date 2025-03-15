package gradle.serialization.serializer

import gradle.serialization.getPolymorphicSerializer
import kotlin.reflect.KClass
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

internal open class JsonPolymorphicSerializer<T : Any>(
    private val baseClass: KClass<T>,
    private val classDiscriminator: String = "type",
) : JsonContentPolymorphicSerializer<T>(baseClass) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<T> {
        val type = element.jsonObject[classDiscriminator]?.jsonPrimitive?.content
            ?: error("Class discriminator '$classDiscriminator' not found in: $element")
        return baseClass.getPolymorphicSerializer(type)
            ?: error("Polymorphic serializer not found for: $element")
    }
}
