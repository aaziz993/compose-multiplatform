package plugin.project.kotlin.ktorfit

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class KtorfitPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.ktorfit
                .takeIf { it.enabled && projectProperties.kotlin.targets?.isNotEmpty()==true }?.let { ktorfit ->
                    plugins.apply(settings.libs.plugins.plugin("ktorfit").id)

                    ktorfit.applyTo()
                }
        }
    }
}
