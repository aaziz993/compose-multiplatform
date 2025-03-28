@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.compose


import gradle.accessors.catalog.libs


import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.compose.model.CMPSettings
import org.gradle.api.Plugin
import org.gradle.api.Project

public class ComposePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.compose
                .takeIf(CMPSettings::enabled)?.let { compose ->
                    plugins.apply(project.settings.libs.plugin("compose.multiplatform").id)
                    plugins.apply(project.settings.libs.plugin("compose.compiler").id)

                    compose.applyTo()
                }
        }
    }
}
