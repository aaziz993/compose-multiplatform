package plugin.project.gradle.apivalidation

import gradle.libs
import gradle.moduleProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class ApiValidationPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (!moduleProperties.settings.gradle.apiValidation.enabled || moduleProperties.targets.isEmpty()) {
                return@with
            }

            // The tool allows dumping binary API of a JVM part of a Kotlin library that is public in the sense of Kotlin visibilities and ensures that the public binary API wasn't changed in a way that makes this change binary incompatible.
            plugins.apply(project.libs.plugins.binary.compatibility.validator.get().pluginId)

            configureApiValidationExtension()
        }
    }
}
