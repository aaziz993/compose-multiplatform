package gradle.plugins.buildconfig

import gradle.accessors.buildConfig
import gradle.accessors.catalog.libs
import gradle.accessors.settings
import gradle.api.applyTo
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class BuildConfigExtension(
    val sourceSets: LinkedHashSet<BuildConfigSourceSet>? = null,
) {

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin("com.github.gmazzo.buildconfig") {
            sourceSets?.forEach { sourceSet ->
                sourceSet.applyTo(project.buildConfig.sourceSets)
            }
        }
}
