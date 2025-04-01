package klib.data.type.serialization.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

public abstract class JsonBaseObjectTransformingSerializer<T : Any>(
    tSerializer: KSerializer<T>,
) : JsonTransformingSerializer<T>(tSerializer) {

    public abstract fun transformDeserialize(key: String, value: JsonElement?): JsonObject

    final override fun transformDeserialize(element: JsonElement): JsonElement =
        if (element is JsonObject) {
            if (element.keys.size == 1) {
                val key = element.keys.single()
                val value = element.values.single()

                JsonObject(
                    buildMap {
                        putAll(transformDeserialize(key, value))
                        putAll(if (value is JsonObject) value.jsonObject else transformDeserialize(key, value))
                    },
                )
            }
            else element
        }
        else transformDeserialize(element.jsonPrimitive.content, null)
}
