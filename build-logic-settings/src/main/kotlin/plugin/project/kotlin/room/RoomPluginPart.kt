package plugin.project.kotlin.room

import gradle.libs
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class RoomPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (projectProperties.plugins.room.enabled || projectProperties.kotlin.hasTargets) {
                return@with
            }

            plugins.apply(project.libs.plugins.room.get().pluginId)

            configureRoomExtension()
        }
    }
}
