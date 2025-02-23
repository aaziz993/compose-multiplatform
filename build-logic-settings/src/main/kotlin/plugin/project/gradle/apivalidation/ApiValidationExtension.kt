package plugin.project.gradle.apivalidation

import gradle.apiValidation
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import kotlinx.validation.ExperimentalBCVApi
import org.gradle.api.Project

@OptIn(ExperimentalBCVApi::class)
internal fun Project.configureApiValidationExtension() =
    pluginManager.withPlugin(settings.libs.plugins.plugin("binary.compatibility.validator").id) {
       projectProperties.plugins.apiValidation.let { apiValidation ->
            apiValidation {
                apiValidation.applyTo(this)
            }
        }
    }

