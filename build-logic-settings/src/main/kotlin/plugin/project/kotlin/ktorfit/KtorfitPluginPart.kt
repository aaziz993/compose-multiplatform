package plugin.project.kotlin.ktorfit

import gradle.libs
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class KtorfitPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (settings.projectProperties.plugins.ktorfit.enabled || settings.projectProperties.kotlin.targets.isEmpty()) {
                return@with
            }

            plugins.apply(project.libs.plugins.ktorfit.get().pluginId)

            configureKtorfitGradleConfiguration()
        }
    }
}
