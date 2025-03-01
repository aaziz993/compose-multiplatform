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
    private val keyAs: String,
    private val valueAs: String? = null,
) : JsonTransformingSerializer<T>(tSerializer) {

    override fun transformDeserialize(element: JsonElement): JsonElement {
        if (element is JsonObject) {
            val key = element.keys.single()
            val value = element.values.single()

            if (value is JsonObject) {
                return JsonObject(
                    buildMap {
                        put(keyAs, JsonPrimitive(key))
                        putAll(value.jsonObject)
                    },
                )
            }

            return JsonObject(
                mapOf(
                    keyAs to JsonPrimitive(key),
                    valueAs!! to value,
                ),
            )
        }

        return JsonObject(
            mapOf(
                keyAs to element,
            ),
        )
    }

    override fun transformSerialize(element: JsonElement): JsonElement =
        JsonObject(
            mapOf(
                element.jsonObject[keyAs]!!.jsonPrimitive.content to
                    if (valueAs == null) JsonObject(element.jsonObject.filterKeys { key -> key != keyAs })
                    else element.jsonObject[valueAs]!!,
            ),
        )
}
