package plugin.project.kotlin.ktorfit

import gradle.libs
import gradle.moduleProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class KtorfitPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (!moduleProperties.settings.kotlin.ktorfit.enabled || moduleProperties.targets.isEmpty()) {
                return@with
            }

            plugins.apply(project.libs.plugins.ktorfit.get().pluginId)

            configureKtorfitGradleConfiguration()
        }
    }
}
