package gradle.plugins.kmp.nat

import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

@Serializable
internal data class Binary(
    val namePrefix: String = "",
    val buildType: NativeBuildType,
)

internal object BinaryContentPolymorphicSerializer : JsonContentPolymorphicSerializer<Any>(
    Any::class,
) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Any> =
        if (element is JsonPrimitive) String.serializer()
        else Binary.serializer()
}
