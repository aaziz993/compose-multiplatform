package gradle.plugins.buildconfig.model


import gradle.accessors.catalog.libs


import gradle.accessors.settings
import gradle.plugins.buildconfig.BuildConfigExtension
import gradle.plugins.buildconfig.BuildConfigSourceSet
import gradle.plugins.buildconfig.BuildConfigSourceSetKeyTransformingSerializer
import gradle.plugins.project.EnabledSettings
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class BuildConfigSettings(
    override val sourceSets: LinkedHashSet<@Serializable(with = BuildConfigSourceSetKeyTransformingSerializer::class) BuildConfigSourceSet>? = null,
    override val enabled: Boolean = true,
) : BuildConfigExtension, EnabledSettings {

    context(Project)
    override fun applyTo() = project.pluginManager.withPlugin(project.settings.libs.plugin("build.config").id) {
        super.applyTo()
    }
}
