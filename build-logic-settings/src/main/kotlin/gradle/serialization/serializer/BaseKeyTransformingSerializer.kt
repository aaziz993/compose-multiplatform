package gradle.serialization.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

internal abstract class BaseKeyTransformingSerializer<T : Any>(
    tSerializer: KSerializer<T>,
) : JsonTransformingSerializer<T>(tSerializer) {

    abstract fun transformKey(key: String, value: JsonElement?): JsonObject

    abstract fun transformValue(key: String, value: JsonElement): JsonObject

    final override fun transformDeserialize(element: JsonElement): JsonElement =
        if (element is JsonObject) {
            println("ELEM:"+element)
            val key = element.keys.single()
            val value = element.values.single()

            JsonObject(
                buildMap {
                    putAll(transformKey(key, value))
                    putAll(if (value is JsonObject) value.jsonObject else transformValue(key, value))
                },
            )
        }
        else transformKey(element.jsonPrimitive.content, null)
}
