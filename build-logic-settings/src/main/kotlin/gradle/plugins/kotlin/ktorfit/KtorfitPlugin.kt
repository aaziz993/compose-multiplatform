package gradle.plugins.kotlin.ktorfit

import gradle.accessors.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class KtorfitPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            // Apply ktorfit properties.
            projectProperties.ktorfit?.applyTo()
        }
    }
}
