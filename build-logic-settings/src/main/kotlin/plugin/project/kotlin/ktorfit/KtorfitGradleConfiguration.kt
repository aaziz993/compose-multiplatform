package plugin.project.kotlin.ktorfit

import gradle.id
import gradle.ktorfit
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Project

internal fun Project.configureKtorfitGradleConfiguration() =
    pluginManager.withPlugin(settings.libs.plugins.plugin("ktorfit").id) {
       projectProperties.plugins.ktorfit.let { ktorfit ->
            ktorfit(ktorfit::applyTo)
        }
    }
