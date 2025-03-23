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
@SerialName("java")
internal data class BuildConfigJavaGenerator(
    val defaultVisibility: Boolean = false,
) : BuildConfigGenerator<com.github.gmazzo.gradle.plugins.generators.BuildConfigJavaGenerator> {

    override fun toBuildConfigGenerator() = com.github.gmazzo.gradle.plugins.generators.BuildConfigJavaGenerator(
        defaultVisibility,
    )

    override fun applyTo(receiver: com.github.gmazzo.gradle.plugins.generators.BuildConfigJavaGenerator) {
        receiver::defaultVisibility trySet defaultVisibility
    }
}

internal object BuildConfigJavaGeneratorSerializer : JsonContentPolymorphicSerializer<Any>(Any::class) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Any> =
        when (element) {
            is JsonPrimitive -> Boolean.serializer()
            else -> BuildConfigJavaGenerator.serializer()
        }
}
