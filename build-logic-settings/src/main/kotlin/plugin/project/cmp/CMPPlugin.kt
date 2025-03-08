@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project.cmp

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

public class CMPPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.compose
                .takeIf { it.enabled && projectProperties.kotlin.targets?.isNotEmpty() == true }?.let { compose ->
                    plugins.apply(settings.libs.plugins.plugin("compose.multiplatform").id)
                    plugins.apply(settings.libs.plugins.plugin("compose.compiler").id)

                    compose.applyTo()
                }
        }
    }
}
