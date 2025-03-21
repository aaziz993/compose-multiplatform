package gradle.plugins.buildconfig

import gradle.accessors.buildConfig
import gradle.api.applyTo
import org.gradle.api.Project

internal interface BuildConfigExtension {

    val sourceSets: List<BuildConfigSourceSet>?

    context(Project)
    fun applyTo() {
        sourceSets?.forEach { sourceSet ->
            sourceSet.applyTo(buildConfig.sourceSets)
        }
    }
}
