package klib.data.type.serialization.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

public open class JsonObjectTransformingSerializer<T : Any>(
    tSerializer: KSerializer<T>,
    public val keyAs: String,
    public val valueAs: String? = null,
) : JsonBaseObjectTransformingSerializer<T>(tSerializer) {

    override fun transformDeserialize(key: String, value: JsonElement?): JsonObject =
        buildJsonObject {
            put(keyAs, JsonPrimitive(key))
            value?.let { value -> put(valueAs!!, value) }
        }
}
