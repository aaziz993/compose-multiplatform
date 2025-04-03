package gradle.plugins.kotlin.room

import gradle.api.project.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class RoomPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            // Apply room properties.
            projectProperties.room?.applyTo()
        }
    }
}
