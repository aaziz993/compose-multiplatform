package gradle.plugins.buildconfig

import gradle.accessors.buildConfig

import gradle.accessors.catalog.libs


import gradle.accessors.settings
import gradle.api.applyTo
import kotlinx.serialization.Serializable
import org.gradle.api.Project

internal interface BuildConfigExtension {

    val sourceSets: LinkedHashSet<@Serializable(with = BuildConfigSourceSetKeyTransformingSerializer::class) BuildConfigSourceSet>?

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugin("buildconfig").id) {
            sourceSets?.forEach { sourceSet ->
                sourceSet.applyTo(project.buildConfig.sourceSets)
            }
        }
}
