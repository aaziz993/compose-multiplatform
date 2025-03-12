package plugin.project.kotlin.cocoapods

import gradle.id
import gradle.libs
import gradle.plugins.kmp.nat.apple.KotlinAppleTarget
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class CocoapodsPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.kotlin.cocoapods
                .takeIf { it.enabled && projectProperties.kotlin.targets.any { target -> target is KotlinAppleTarget } == true }?.let { cocoapods ->
                    plugins.apply(settings.libs.plugins.plugin("cocoapods").id)

                    cocoapods.applyTo()
                }
        }
    }
}
