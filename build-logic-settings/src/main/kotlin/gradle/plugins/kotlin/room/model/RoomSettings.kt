package gradle.plugins.kotlin.room.model

import gradle.accessors.catalog.libs
import gradle.accessors.settings
import gradle.plugins.kotlin.room.RoomExtension
import gradle.plugins.kotlin.room.SchemaDirectory
import gradle.api.EnabledSettings
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class RoomSettings(
    override val schemaDirectories: Set<SchemaDirectory>? = null,
    override val generateKotlin: Boolean? = null,
    override val enabled: Boolean = true
) : RoomExtension, EnabledSettings {

    context(Project)
    override fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugin("room").id) {
            super.applyTo()
        }
}
