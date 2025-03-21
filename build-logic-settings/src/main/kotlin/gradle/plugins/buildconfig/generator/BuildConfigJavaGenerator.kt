package gradle.plugins.buildconfig.generator

import com.github.gmazzo.gradle.plugins.generators.BuildConfigJavaGenerator
import gradle.api.trySet
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("java")
internal data class BuildConfigJavaGenerator(
    val defaultVisibility: Boolean = false,
) : BuildConfigGenerator<BuildConfigJavaGenerator> {

    override fun toBuildConfigGenerator() = BuildConfigJavaGenerator(
            defaultVisibility,
    )

    override fun applyTo(recipient: BuildConfigJavaGenerator) {
        recipient::defaultVisibility trySet defaultVisibility
    }
}
