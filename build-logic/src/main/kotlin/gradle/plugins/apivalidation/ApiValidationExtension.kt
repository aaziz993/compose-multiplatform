package gradle.plugins.apivalidation

import kotlinx.validation.ApiValidationExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the

public val Project.apiValidation: ApiValidationExtension get() = the()

public fun Project.apiValidation(configure: ApiValidationExtension.() -> Unit): Unit =
    extensions.configure(configure)
