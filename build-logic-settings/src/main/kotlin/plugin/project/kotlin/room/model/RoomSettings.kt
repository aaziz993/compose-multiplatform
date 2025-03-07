package plugin.project.kotlin.room.model

import gradle.id
import gradle.libs
import gradle.model.kotlin.room.RoomExtension
import gradle.plugin
import gradle.plugins
import gradle.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import gradle.model.project.EnabledSettings

@Serializable
internal data class RoomSettings(
    override val schemaDirectories: Set<String>? = null,
    override val generateKotlin: Boolean? = null,
    override val enabled: Boolean = true
) : RoomExtension, EnabledSettings {

    context(Project)
    override fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("room").id) {
            super.applyTo()
        }
}
