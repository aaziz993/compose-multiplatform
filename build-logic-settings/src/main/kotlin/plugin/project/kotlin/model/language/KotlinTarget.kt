package plugin.project.kotlin.model.language

import gradle.serialization.getPolymorphicSerializer
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.jetbrains.amper.frontend.Platform

@Serializable(with = KotlinTargetSerializer::class)
internal interface KotlinTarget {

    val name: String?
}

internal object KotlinTargetPolymorphicSerializer : JsonContentPolymorphicSerializer<KotlinTarget>(KotlinTarget::class) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<KotlinTarget> {
        val type = element.jsonObject["type"]!!.jsonPrimitive.content
        return KotlinTarget::class.getPolymorphicSerializer(type)!!
        Platform
    }
}

internal object KotlinTargetSerializer :
    JsonTransformingSerializer<KotlinTarget>(KotlinTargetPolymorphicSerializer) {

    override fun transformDeserialize(element: JsonElement): JsonElement {
        if (element is JsonObject) {
            val key = element.keys.single()
            val value = element.values.single()
            JsonObject(
                buildMap {
                    put("type", JsonPrimitive(key))
                    putAll(value.jsonObject)
                },
            )
        }

        return JsonObject(
            mapOf(
                "type" to element,
            ),
        )
    }
}
