package klib.data.type.serialization.json.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.elementNames
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

public abstract class JsonBaseObjectTransformingSerializer<T : Any>(
    private val tSerializer: KSerializer<T>,
) : JsonTransformingSerializer<T>(tSerializer) {

    public abstract fun transformDeserialize(key: String, value: JsonElement?): JsonObject

    final override fun transformDeserialize(element: JsonElement): JsonElement =
        if (element is JsonObject) {
            val key = element.keys.single()
            if (element.keys.size == 1 && key !in tSerializer.descriptor.elementNames) {
                val value = element.values.single()

                JsonObject(
                    transformDeserialize(key, value) +
                        if (value is JsonObject) value.jsonObject else transformDeserialize(key, value),
                )
            }
            else element
        }
        else transformDeserialize(element.jsonPrimitive.content, null)
}
