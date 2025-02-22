package plugin.project.gradle.apivalidation

import gradle.apiValidation
import gradle.libs
import gradle.projectProperties
import gradle.settings
import kotlinx.validation.ExperimentalBCVApi
import org.gradle.api.Project

@OptIn(ExperimentalBCVApi::class)
internal fun Project.configureApiValidationExtension() =
    pluginManager.withPlugin(libs.plugins.binary.compatibility.validator.get().pluginId) {
       settings.projectProperties.plugins.apiValidation.let { apiValidation ->
            apiValidation {
                apiValidation.applyTo(this)
            }
        }
    }

