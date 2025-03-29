package gradle.plugins.kotlin.cocoapods

import gradle.accessors.catalog.libs

import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.kotlin.cocoapods.model.CocoapodsSettings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class CocoapodsPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.kotlin.cocoapods
                .takeIf(CocoapodsSettings::enabled)?.let { cocoapods ->
                    plugins.apply(project.settings.libs.plugin("cocoapods").id)

                    cocoapods.applyTo()
                }
        }
    }
}
