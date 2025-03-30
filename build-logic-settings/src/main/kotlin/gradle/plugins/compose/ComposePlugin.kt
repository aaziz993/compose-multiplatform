@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.compose

import gradle.accessors.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

public class ComposePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.compose?.takeIf {
                pluginManager.hasPlugin("org.jetbrains.compose") &&
                    pluginManager.hasPlugin("org.jetbrains.kotlin.plugin.compose")
            }?.applyTo()
        }
    }
}
