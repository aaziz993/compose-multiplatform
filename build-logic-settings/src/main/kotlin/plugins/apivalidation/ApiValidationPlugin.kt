package plugins.apivalidation

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class ApiValidationPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.apiValidation
                .takeIf { it.enabled && projectProperties.kotlin.targets.isNotEmpty() }?.let { apiValidation ->
                    // The tool allows dumping binary API of a JVM part of a Kotlin library that is public in the sense of Kotlin visibilities and ensures that the public binary API wasn't changed in a way that makes this change binary incompatible.
                    plugins.apply(project.settings.libs.plugins.plugin("binary.compatibility.validator").id)

                    apiValidation.applyTo()
                }
        }
    }
}
