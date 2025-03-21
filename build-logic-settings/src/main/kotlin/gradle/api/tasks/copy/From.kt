package gradle.api.tasks.copy

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.gradle.internal.impldep.kotlinx.serialization.json.JsonContentPolymorphicSerializer

@Serializable
internal data class From(
    val sourcePath: String,
    val copySpec: CopySpecImpl,
)

internal object FromSerializer : JsonContentPolymorphicSerializer<Any>(Any::class) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Any> =
        when (element) {
            is JsonPrimitive -> String.serializer()
            is JsonArray -> SetSerializer(String.serializer())
            is JsonObject -> From.serializer()
        }
}
