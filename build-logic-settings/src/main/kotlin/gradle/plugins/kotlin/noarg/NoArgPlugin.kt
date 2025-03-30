package gradle.plugins.kotlin.noarg

import gradle.accessors.catalog.libs

import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.kotlin.noarg.model.NoArgSettings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class NoArgPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.noArg?.takeIf{ pluginManager.hasPlugin("noArg") }?.let { noArg ->
                    plugins.apply(project.settings.libs.plugin("allopen").id)

                    noArg.applyTo()
                }
        }
    }
}
