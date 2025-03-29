package gradle.serialization.serializer

import kotlin.reflect.KClass
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

public abstract class KeyTransformingSerializer<T : Any>(
    tSerializer: KSerializer<T>,
    public val keyAs: String,
    public val valueAs: String? = null,
) : BaseKeyTransformingSerializer<T>(tSerializer) {

    override fun transformKey(key: String, value: JsonElement?): JsonObject = JsonObject(
        mapOf(
            keyAs to JsonPrimitive(key),
        ),
    )

    override fun transformValue(key: String, value: JsonElement): JsonObject = JsonObject(
        valueAs?.let { valueAs ->
            mapOf(valueAs to value)
        } ?: emptyMap(),
    )
}
