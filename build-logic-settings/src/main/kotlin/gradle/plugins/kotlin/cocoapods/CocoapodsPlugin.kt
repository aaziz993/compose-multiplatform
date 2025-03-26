package gradle.plugins.kotlin.cocoapods

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.kmp.nat.apple.KotlinAppleTarget
import gradle.plugins.kotlin.cocoapods.model.CocoapodsSettings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class CocoapodsPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.kotlin.cocoapods
                .takeIf(CocoapodsSettings::enabled)?.let { cocoapods ->
                    plugins.apply(project.settings.libs.plugins.plugin("cocoapods").id)

                    cocoapods.applyTo()
                }
        }
    }
}
