package gradle.serialization.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonObject

internal abstract class DelegateTransformingSerializer<T : Any>(
    tSerializer: KSerializer<T>,
    private val delegateName: String = "delegate"
) : JsonTransformingSerializer<T>(tSerializer) {

    override fun transformSerialize(element: JsonElement): JsonElement = element.jsonObject[delegateName]!!

    override fun transformDeserialize(element: JsonElement): JsonElement = JsonObject(
        mapOf(delegateName to element),
    )
}
