package plugin.project.kotlin.ktorfit

import gradle.ktorfit
import gradle.libs
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Project

internal fun Project.configureKtorfitGradleConfiguration() =
    pluginManager.withPlugin(libs.plugins.ktorfit.get().pluginId) {
       settings.projectProperties.plugins.ktorfit.let { ktorfit ->
            ktorfit(ktorfit::applyTo)
        }
    }
