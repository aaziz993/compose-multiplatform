package gradle.plugins.kotlin.allopen

import gradle.accessors.catalog.libs

import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.kotlin.allopen.model.AllOpenSettings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class AllOpenPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.allOpen?.takeIf{ pluginManager.hasPlugin("allOpen") }?.let { allOpen ->
                    plugins.apply(project.settings.libs.plugin("allopen").id)

                    allOpen.applyTo()
                }
        }
    }
}
