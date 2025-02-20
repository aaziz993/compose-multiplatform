package plugin.project.kotlin.room

import gradle.moduleProperties
import gradle.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class RoomPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (!moduleProperties.settings.kotlin.room.enabled || moduleProperties.targets.isEmpty()) {
                return@with
            }

            plugins.apply(project.libs.plugins.room.get().pluginId)

            configureRoomExtension()
        }
    }
}
