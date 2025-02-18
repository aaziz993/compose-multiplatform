package plugin.project.kotlin.room

import androidx.room.gradle.RoomGradlePlugin
import gradle.amperModuleExtraProperties
import gradle.room
import gradle.trySet
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal fun Project.configureRoomExtension() =
    plugins.withType<RoomGradlePlugin> {
        amperModuleExtraProperties.settings.kotlin.room.let { room ->
            room {
                room.schemaDirectories?.forEach(::schemaDirectory)
                ::generateKotlin trySet room.generateKotlin
            }
        }
    }
