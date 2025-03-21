package gradle.plugins.buildconfig.generator

import gradle.api.trySet
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import org.gradle.internal.impldep.kotlinx.serialization.json.JsonContentPolymorphicSerializer

@Serializable
@SerialName("kotlin")
internal data class BuildConfigKotlinGenerator(
    val topLevelConstants: Boolean = false,
    val internalVisibility: Boolean = true
) : BuildConfigGenerator<com.github.gmazzo.gradle.plugins.generators.BuildConfigKotlinGenerator> {

    override fun toBuildConfigGenerator() = com.github.gmazzo.gradle.plugins.generators.BuildConfigKotlinGenerator(
        topLevelConstants,
        internalVisibility,
    )

    override fun applyTo(recipient: com.github.gmazzo.gradle.plugins.generators.BuildConfigKotlinGenerator) {
        recipient::topLevelConstants trySet topLevelConstants
        recipient::internalVisibility trySet internalVisibility
    }
}

internal object BuildConfigKotlinGeneratorSerializer : JsonContentPolymorphicSerializer<Any>(Any::class) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Any> =
        when (element) {
            is JsonPrimitive -> Boolean.serializer()
            else -> BuildConfigKotlinGenerator.serializer()
        }
}
