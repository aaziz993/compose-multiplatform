package plugin.project.kotlin.room

import gradle.libs
import gradle.projectProperties
import gradle.room
import gradle.settings
import org.gradle.api.Project

internal fun Project.configureRoomExtension() =
    pluginManager.withPlugin(libs.plugins.room.get().pluginId) {
       settings.projectProperties.plugins.room.let { room ->
            room(room::applyTo)
        }
    }
