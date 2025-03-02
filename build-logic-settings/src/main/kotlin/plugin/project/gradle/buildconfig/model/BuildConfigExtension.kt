package plugin.project.gradle.buildconfig.model

import gradle.buildConfig
import org.gradle.api.Project

internal interface BuildConfigExtension {

    val sourceSets: List<String>?

    context(Project)
    fun applyTo() {
        sourceSets?.forEach(buildConfig.sourceSets::register)
    }
}
