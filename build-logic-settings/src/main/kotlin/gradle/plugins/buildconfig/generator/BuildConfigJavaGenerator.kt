package gradle.plugins.buildconfig.generator

import klib.data.type.reflection.trySet
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
