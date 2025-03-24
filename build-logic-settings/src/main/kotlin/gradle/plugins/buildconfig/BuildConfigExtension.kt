package gradle.plugins.buildconfig

import gradle.accessors.buildConfig
import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.api.applyTo
import org.gradle.api.Project

internal interface BuildConfigExtension {

    val sourceSets: List<BuildConfigSourceSet>?

    context(project: Project)
    fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugins.plugin("buildconfig").id) {
            sourceSets?.forEach { sourceSet ->
                sourceSet.applyTo(project.buildConfig.sourceSets)
            }
        }
}
