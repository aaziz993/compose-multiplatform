@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.compose

import gradle.api.project.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

public class ComposePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            // Apply compose properties.
            projectProperties.compose?.applyTo()
        }
    }
}
