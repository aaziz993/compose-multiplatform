package gradle.plugins.buildconfig.generator

import klib.data.type.reflection.trySet
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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

    override fun applyTo(receiver: com.github.gmazzo.gradle.plugins.generators.BuildConfigKotlinGenerator) {
        receiver::topLevelConstants trySet topLevelConstants
        receiver::internalVisibility trySet internalVisibility
    }
}
