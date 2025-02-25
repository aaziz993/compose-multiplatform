package plugin.model.dependency

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

internal object ProjectDependencyPolymorphicSerializer : JsonContentPolymorphicSerializer<ProjectDependency>(ProjectDependency::class) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<ProjectDependency> {
        val type = element.jsonObject["type"]?.jsonPrimitive?.content

        return when {
            type == null -> Dependency.serializer()
            type.endsWith("npm", true) -> NpmDependency.serializer()
            type == "pod" -> PodDependency.serializer()

            else -> error("Unsupported dependency configuration: $type")
        }
    }
}

internal object ProjectDependencySerializer :
    JsonTransformingSerializer<ProjectDependency>(ProjectDependencyPolymorphicSerializer) {

    override fun transformDeserialize(element: JsonElement): JsonElement {
        if (element is JsonObject) {
            val key = element.keys.single()
            val value = element.values.single()

            when {
                key.endsWith("npm", true) || key == "pod" -> {
                    val npmConfiguration = if (key.endsWith("npm")) mapOf(
                        "npmConfiguration" to JsonPrimitive(key),
                    )
                    else emptyMap()

                    if (value is JsonObject) {
                        return JsonObject(
                            buildMap {
                                put("type", JsonPrimitive(key))
                                putAll(npmConfiguration)
                                putAll(value.jsonObject)
                            },
                        )
                    }

                    return JsonObject(
                        mapOf(
                            "type" to JsonPrimitive(key),
                            "notation" to value,
                        ) + npmConfiguration,
                    )
                }

                else -> return JsonObject(
                    mapOf(
                        "notation" to JsonPrimitive(key),
                        "configuration" to value,
                    ),
                )
            }
        }

        return JsonObject(
            mapOf(
                "notation" to element,
            ),
        )
    }
}
