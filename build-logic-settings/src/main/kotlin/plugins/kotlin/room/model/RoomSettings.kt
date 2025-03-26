package plugins.kotlin.room.model

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.plugins.kotlin.room.RoomExtension
import gradle.plugins.kotlin.room.SchemaDirectory
import gradle.plugins.kotlin.room.SchemaDirectoryTransformingSerializer
import gradle.project.EnabledSettings
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class RoomSettings(
    override val schemaDirectories: Set<@Serializable(with = SchemaDirectoryKeyTransformingSerializer::class) SchemaDirectory>? = null,
    override val generateKotlin: Boolean? = null,
    override val enabled: Boolean = true
) : RoomExtension, EnabledSettings {

    context(Project)
    override fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugins.plugin("room").id) {
            super.applyTo()
        }
}
