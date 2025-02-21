package plugin.project.kotlin.room

import androidx.room.gradle.RoomGradlePlugin
import gradle.libs
import gradle.moduleProperties
import gradle.room
import gradle.trySet
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal fun Project.configureRoomExtension() =
    pluginManager.withPlugin(libs.plugins.room.get().pluginId) {
        moduleProperties.settings.kotlin.room.let { room ->
            room(room::applyTo)
        }
    }
