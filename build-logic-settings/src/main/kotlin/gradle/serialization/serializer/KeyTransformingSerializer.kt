package gradle.serialization.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

internal abstract class BaseKeyTransformingSerializer<T : Any>(
    tSerializer: KSerializer<T>,
) : JsonTransformingSerializer<T>(tSerializer) {

    abstract fun transformKey(key: String, value: JsonElement?): JsonObject

    abstract fun transformValue(key: String, value: String): JsonObject

    final override fun transformDeserialize(element: JsonElement): JsonElement =
        if (element is JsonObject) {
            val key = element.keys.single()
            val value = element.values.single()

            JsonObject(
                buildMap {
                    putAll(transformKey(key, value))
                    when (value) {
                        is JsonPrimitive -> putAll(transformValue(key, value.jsonPrimitive.content))
                        is JsonObject -> putAll(value.jsonObject)
                        is JsonArray -> throw UnsupportedOperationException("Value can't be array")
                    }
                },
            )
        }
        else transformKey(element.jsonPrimitive.content, null)
}

internal abstract class KeyTransformingSerializer<T : Any>(
    tSerializer: KSerializer<T>,
    val keyAs: String,
    val valueAs: String? = null,
) : BaseKeyTransformingSerializer<T>(tSerializer) {

    override fun transformKey(key: String, value: JsonElement?): JsonObject = JsonObject(
        mapOf(
            keyAs to JsonPrimitive(key),
        ),
    )

    override fun transformValue(key: String, value: String): JsonObject = JsonObject(
        valueAs?.let { valueAs ->
            mapOf(valueAs to JsonPrimitive(value))
        } ?: emptyMap(),
    )
}
