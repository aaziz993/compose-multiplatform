package gradle.serialization.serializer

import gradle.serialization.getPolymorphicSerializer
import kotlin.reflect.KClass
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

internal open class JsonContentPolymorphicSerializer<T : Any>(
    private val baseClass: KClass<T>,
    private val classDiscriminator: String = "type",
) : JsonContentPolymorphicSerializer<T>(baseClass) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<T> {
        println(element)
        val type = element.jsonObject[classDiscriminator]!!.jsonPrimitive.content
        return baseClass.getPolymorphicSerializer(type)!!
    }
}
