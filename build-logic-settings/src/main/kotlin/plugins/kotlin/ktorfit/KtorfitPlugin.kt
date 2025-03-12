package plugins.kotlin.ktorfit

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class KtorfitPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.ktorfit
                .takeIf { it.enabled && projectProperties.kotlin.targets.isNotEmpty() }?.let { ktorfit ->
                    plugins.apply(settings.libs.plugins.plugin("ktorfit").id)

                    ktorfit.applyTo()
                }
        }
    }
}
