package gradle.plugins.apivalidation

import gradle.accessors.catalog.libs

import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.apivalidation.model.ApiValidationSettings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class ApiValidationPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.apiValidation
                .takeIf(ApiValidationSettings::enabled)?.let { apiValidation ->
                    // The tool allows dumping binary API of a JVM part of a Kotlin library that is public in the sense of Kotlin visibilities and ensures that the public binary API wasn't changed in a way that makes this change binary incompatible.
                    plugins.apply(project.settings.libs.plugin("binary.compatibility.validator").id)

                    apiValidation.applyTo()
                }
        }
    }
}
