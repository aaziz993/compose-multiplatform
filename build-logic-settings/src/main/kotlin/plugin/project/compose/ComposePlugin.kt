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
import plugin.project.compose.android.configureAndroidExtension
import plugin.project.compose.desktop.configureDesktopExtension
import plugin.project.compose.resources.configureResourcesExtension
import plugin.project.model.ProjectType

public class ComposePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.compose
                .takeIf { it.enabled && projectProperties.kotlin.targets?.isNotEmpty() == true }?.let { compose ->
                    plugins.apply(settings.libs.plugins.plugin("compose.multiplatform").id)
                    plugins.apply(settings.libs.plugins.plugin("compose.compiler").id)

                    if (projectProperties.type == ProjectType.APP) {
                        configureDesktopExtension()
                        configureAndroidExtension()
                    }

                    configureResourcesExtension()
                }
        }
    }
}
