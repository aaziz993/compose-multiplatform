@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project.compose

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import plugin.project.compose.desktop.configureDesktopExtension

public class ComposePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (projectProperties.compose.enabled || projectProperties.kotlin.hasTargets) {
                return@with
            }

            plugins.apply(settings.libs.plugins.plugin("compose.multiplatform").id)
            plugins.apply(settings.libs.plugins.plugin("compose.compiler").id)

            if (projectProperties.application) {
                configureDesktopExtension()
            }

            configureResourcesExtension()
        }
    }
}
