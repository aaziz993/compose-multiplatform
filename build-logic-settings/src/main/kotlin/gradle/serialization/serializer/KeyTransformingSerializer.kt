package gradle.serialization.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

internal abstract class KeyTransformingSerializer<T : Any>(
    tSerializer: KSerializer<T>,
    private val keyTransform: (key: String, value: JsonElement?) -> String,
    private val valueTransform: (key: String, value: JsonElement) -> String,
) : JsonTransformingSerializer<T>(tSerializer) {

    constructor(
        tSerializer: KSerializer<T>,
        keyAs: String,
        valueAs: String? = null,
    ) : this(
        tSerializer,
        { _, _ -> keyAs },
        { _, _ -> valueAs!! },
    )

    override fun transformDeserialize(element: JsonElement): JsonElement =
        if (element is JsonObject) {
            val key = element.keys.single()
            val value = element.values.single()

            JsonObject(
                buildMap {
                    put(keyTransform(key, value), JsonPrimitive(key))
                    if (value is JsonObject) putAll(value.jsonObject)
                    else put(valueTransform(key, value)!!, value)
                },
            )
        }
        else JsonObject(
            mapOf(
                keyTransform(element.jsonPrimitive.content, null) to element,
            ),
        )
}
