package gradle.api.tasks.copy

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import org.gradle.internal.impldep.kotlinx.serialization.json.JsonContentPolymorphicSerializer

@Serializable
internal data class From(
    val sourcePath: String,
    val copySpec: CopySpecImpl,
)

internal object FromContentPolymorphicSerializer : JsonContentPolymorphicSerializer<Any>(Any::class) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Any> =
        if (element is JsonPrimitive) String.serializer()
        else From.serializer()
}
