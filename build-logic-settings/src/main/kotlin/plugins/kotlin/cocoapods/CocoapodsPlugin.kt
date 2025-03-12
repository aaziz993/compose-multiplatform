package plugins.kotlin.cocoapods

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.kmp.nat.apple.KotlinAppleTarget
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
