@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project.compose

import gradle.libs
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import plugin.project.compose.desktop.configureDesktopExtension

public class ComposePluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (projectProperties.compose.enabled || projectProperties.kotlin.hasTargets) {
                return@with
            }

            plugins.apply(libs.plugins.compose.multiplatform.get().pluginId)
            plugins.apply(libs.plugins.compose.compiler.get().pluginId)

            if (projectProperties.application) {
                configureDesktopExtension()
            }

            configureResourcesExtension()
        }
    }
}
