@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project.compose

import gradle.libs
import gradle.moduleProperties
import org.gradle.api.Plugin
import org.gradle.api.Project
import plugin.project.compose.desktop.configureDesktopExtension

public class ComposePluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (!moduleProperties.settings.compose.enabled || moduleProperties.targets.isEmpty()) {
                return@with
            }

            plugins.apply(libs.plugins.compose.multiplatform.get().pluginId)
            plugins.apply(libs.plugins.compose.compiler.get().pluginId)

            if (moduleProperties.application) {
                configureDesktopExtension()
            }

            configureResourcesExtension()
        }
    }
}
