package gradle.plugins.kotlin.cocoapods

import gradle.api.project.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class CocoapodsPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            // Apply cocoapods properties.
            projectProperties.kotlin?.cocoapods?.applyTo()
        }
    }
}
