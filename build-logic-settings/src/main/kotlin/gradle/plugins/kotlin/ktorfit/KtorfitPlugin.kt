package gradle.plugins.kotlin.ktorfit

import gradle.accessors.catalog.libs

import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.kotlin.ktorfit.model.KtorfitSettings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class KtorfitPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.ktorfit?.takeIf{ pluginManager.hasPlugin("ktorfit") }?.let { ktorfit ->
                    plugins.apply(project.settings.libs.plugin("ktorfit").id)

                    ktorfit.applyTo()
                }
        }
    }
}
