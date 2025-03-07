package gradle.model.gradle.buildconfig

import gradle.buildConfig
import org.gradle.api.Project

internal interface BuildConfigExtension {

    val sourceSets: List<String>?

    context(Project)
    fun applyTo() {
        sourceSets?.forEach(buildConfig.sourceSets::register)
    }
}
