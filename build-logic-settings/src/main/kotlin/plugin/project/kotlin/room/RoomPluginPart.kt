package plugin.project.kotlin.room

import gradle.moduleProperties
import gradle.libs
import plugin.project.BindingPluginPart
import org.gradle.api.Project

internal class RoomPluginPart(override val project: Project) : BindingPluginPart {

    override val needToApply: Boolean by lazy {
        project.moduleProperties.settings.kotlin.room.enabled
    }

    override fun applyAfterEvaluate() = with(project) {
        plugins.apply(project.libs.plugins.room.get().pluginId)

        applySettings()
    }

    private fun applySettings() {
        with(project) {
            configureRoomExtension()
        }
    }
}
