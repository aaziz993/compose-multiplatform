package plugin.project.kotlin.kmp.model

import gradle.serialization.serializer.JsonPolymorphicTransformingSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import plugin.project.kotlin.model.language.KotlinSourceSet

@Serializable(with = HierarchyGroupSerializer::class)
internal data class HierarchyGroup(
    val alias: String,
    val group: List<String>
)

private object HierarchyGroupSerializer : JsonPolymorphicTransformingSerializer<HierarchyGroup>(
    HierarchyGroup::class,
) {

    override fun transformDeserialize(element: JsonElement): JsonElement {
        val jsonObject = element.jsonObject
        val key = jsonObject.keys.single()
        val value = jsonObject.values.single()
        return JsonObject(
            buildMap {
                put("type", JsonPrimitive(key))
                putAll(value.jsonObject)
            },
        )
    }
}
