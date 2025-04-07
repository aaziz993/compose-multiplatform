package klib.data.type.serialization.json.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.elementNames
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

public open class JsonObjectTransformingSerializer<T : Any>(
    tSerializer: KSerializer<T>,
    private val keyAs: String,
    private val valueAs: String? = null,
) : JsonBaseObjectTransformingSerializer<T>(tSerializer) {

    override fun transformDeserialize(key: String, value: JsonElement?): JsonObject =
        buildJsonObject {
            put(keyAs, JsonPrimitive(key))
            value?.let { value -> put(valueAs!!, value) }
        }
}
