package gradle.plugins.buildconfig

import gradle.accessors.buildConfig
import org.gradle.api.Project

internal interface BuildConfigExtension {

    val sourceSets: List<String>?

    context(Project)
    fun applyTo() {
        sourceSets?.forEach(buildConfig.sourceSets::register)
    }
}
