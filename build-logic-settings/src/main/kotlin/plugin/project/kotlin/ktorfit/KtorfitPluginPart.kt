package plugin.project.kotlin.ktorfit

import gradle.libs
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class KtorfitPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (projectProperties.plugins.ktorfit.enabled || projectProperties.kotlin.hasTargets) {
                return@with
            }

            plugins.apply(project.libs.plugins.ktorfit.get().pluginId)

            configureKtorfitGradleConfiguration()
        }
    }
}
