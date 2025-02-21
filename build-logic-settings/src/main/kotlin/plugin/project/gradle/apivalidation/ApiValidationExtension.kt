package plugin.project.gradle.apivalidation

import gradle.apiValidation
import gradle.libs
import gradle.moduleProperties
import gradle.trySet
import kotlinx.validation.BinaryCompatibilityValidatorPlugin
import kotlinx.validation.ExperimentalBCVApi
import kotlinx.validation.KotlinApiBuildTask
import kotlinx.validation.api.klib.KlibSignatureVersion
import org.gradle.api.Project
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.withType

@OptIn(ExperimentalBCVApi::class)
internal fun Project.configureApiValidationExtension() =
    pluginManager.withPlugin(libs.plugins.binary.compatibility.validator.get().pluginId) {
        moduleProperties.settings.gradle.apiValidation.let { apiValidation ->
            apiValidation {
                apiValidation.applyTo(this)
            }
        }
    }

