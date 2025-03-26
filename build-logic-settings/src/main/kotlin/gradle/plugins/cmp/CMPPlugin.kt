@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.cmp

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import gradle.plugins.cmp.model.CMPSettings

public class CMPPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.compose
                .takeIf(CMPSettings::enabled)?.let { compose ->
                    plugins.apply(project.settings.libs.plugins.plugin("compose.multiplatform").id)
                    plugins.apply(project.settings.libs.plugins.plugin("compose.compiler").id)

                    compose.applyTo()
                }
        }
    }
}
