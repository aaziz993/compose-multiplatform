package plugins.kotlin.room

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class RoomPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.room
                .takeIf { it.enabled && projectProperties.kotlin.targets.isNotEmpty() }?.let { room ->
                    plugins.apply(project.settings.libs.plugins.plugin("room").id)

                    room.applyTo()
                }
        }
    }
}
