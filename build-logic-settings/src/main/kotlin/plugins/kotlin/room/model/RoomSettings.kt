package plugins.kotlin.room.model

import gradle.accessors.id
import gradle.accessors.libs
import gradle.plugins.kotlin.room.RoomExtension
import gradle.plugins.project.EnabledSettings
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project

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
