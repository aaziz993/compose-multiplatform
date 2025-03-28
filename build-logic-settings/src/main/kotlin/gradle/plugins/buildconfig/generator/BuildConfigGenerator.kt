package gradle.plugins.buildconfig.generator

import gradle.serialization.serializer.JsonContentPolymorphicSerializer
import kotlinx.serialization.Serializable

@Serializable(with = BuildConfigGeneratorSerializer::class)
internal interface BuildConfigGenerator<T : com.github.gmazzo.gradle.plugins.generators.BuildConfigGenerator> {

    fun toBuildConfigGenerator(): com.github.gmazzo.gradle.plugins.generators.BuildConfigGenerator

    fun applyTo(receiver: T)
}

private object BuildConfigGeneratorSerializer : JsonContentPolymorphicSerializer<BuildConfigGenerator<*>>(
    BuildConfigGenerator::class,
)
