package gradle.plugins.kotlin.room

import gradle.accessors.catalog.libs

import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.kotlin.room.model.RoomSettings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class RoomPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.room
                .takeIf(RoomSettings::enabled)?.let { room ->
                    plugins.apply(project.settings.libs.plugin("room").id)

                    room.applyTo()
                }
        }
    }
}
