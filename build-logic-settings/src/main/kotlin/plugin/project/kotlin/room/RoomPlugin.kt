package plugin.project.kotlin.room

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class RoomPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.room
                .takeIf { it.enabled && projectProperties.kotlin.targets.isNotEmpty() }?.let { room ->
                    plugins.apply(settings.libs.plugins.plugin("room").id)

                    room.applyTo()
                }
        }
    }
}
