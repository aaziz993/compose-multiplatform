package gradle.serialization.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

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
