package gradle.plugins.buildconfig.generator

import com.github.gmazzo.gradle.plugins.generators.BuildConfigKotlinGenerator
import gradle.api.trySet
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("kotlin")
internal data class BuildConfigKotlinGenerator(
    val topLevelConstants: Boolean = false,
    val internalVisibility: Boolean = true
) : BuildConfigGenerator<BuildConfigKotlinGenerator> {

    override fun toBuildConfigGenerator() = BuildConfigKotlinGenerator(
            topLevelConstants,
            internalVisibility,
    )

    override fun applyTo(recipient: BuildConfigKotlinGenerator) {
        recipient::topLevelConstants trySet topLevelConstants
        recipient::internalVisibility trySet internalVisibility
    }
}
