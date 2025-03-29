package gradle.serialization.serializer

import kotlin.reflect.KClass
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonTransformingSerializer

public open class JsonValueTransformingContentPolymorphicSerializer<T : Any>(
    baseClass: KClass<T>,
    classDiscriminator: String = "type",
    private val valueAs: String = "type",
) : JsonTransformingSerializer<T>(JsonContentPolymorphicSerializer(baseClass, classDiscriminator)) {

    override fun transformDeserialize(element: JsonElement): JsonElement =
        if (element is JsonPrimitive) JsonObject(
            mapOf(valueAs to element),
        )
        else element
}
