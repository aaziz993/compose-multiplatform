package gradle.plugins.buildconfig.generator

import klib.data.type.serialization.serializer.JsonContentPolymorphicSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

@Serializable(with = BuildConfigGeneratorContentPolymorphicSerializer::class)
internal interface BuildConfigGenerator<T : com.github.gmazzo.gradle.plugins.generators.BuildConfigGenerator> {

    fun toBuildConfigGenerator(): com.github.gmazzo.gradle.plugins.generators.BuildConfigGenerator

    fun applyTo(receiver: T)
}

private class BuildConfigGeneratorContentPolymorphicSerializer(serializer: KSerializer<Nothing>)
    : JsonContentPolymorphicSerializer<BuildConfigGenerator<*>>(
    BuildConfigGenerator::class,
)
