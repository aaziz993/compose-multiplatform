package plugin.project.kotlin.ktorfit

import de.jensklingenberg.ktorfit.gradle.KtorfitGradlePlugin
import gradle.ktorfit
import gradle.libs
import gradle.moduleProperties
import gradle.trySet
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal fun Project.configureKtorfitGradleConfiguration() =
    pluginManager.withPlugin(libs.plugins.ktorfit.get().pluginId) {
        moduleProperties.settings.kotlin.ktorfit.let { ktorfit ->
            ktorfit(ktorfit::applyTo)
        }
    }
