package plugin.project.kotlin.ktorfit

import gradle.moduleProperties
import gradle.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class KtorfitPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (!moduleProperties.settings.kotlin.ktorfit.enabled || moduleProperties.targets == null) {
                return@with
            }

            plugins.apply(project.libs.plugins.ktorfit.get().pluginId)

            configureKtorfitGradleConfiguration()
        }
    }
}
