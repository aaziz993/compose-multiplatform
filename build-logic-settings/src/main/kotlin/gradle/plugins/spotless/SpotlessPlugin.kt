package gradle.plugins.spotless

import gradle.api.project.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class SpotlessPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            //Apply spotless properties.
            projectProperties.spotless?.applyTo()
        }
    }
}
