package plugin.project.kotlin.room

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.room
import gradle.settings
import org.gradle.api.Project

internal fun Project.configureRoomExtension() =
    pluginManager.withPlugin(settings.libs.plugins.plugin("room").id) {
       projectProperties.plugins.room.let { room ->
            room(room::applyTo)
        }
    }
