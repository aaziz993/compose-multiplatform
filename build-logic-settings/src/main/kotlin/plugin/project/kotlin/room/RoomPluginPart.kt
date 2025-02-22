package plugin.project.kotlin.room

import gradle.libs
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class RoomPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (settings.projectProperties.plugins.room.enabled || settings.projectProperties.kotlin.targets.isEmpty()) {
                return@with
            }

            plugins.apply(project.libs.plugins.room.get().pluginId)

            configureRoomExtension()
        }
    }
}
