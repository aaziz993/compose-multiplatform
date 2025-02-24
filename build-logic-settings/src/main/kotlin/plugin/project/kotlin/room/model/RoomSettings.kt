package plugin.project.kotlin.room.model

import gradle.trySet
import kotlinx.serialization.Serializable

@Serializable
internal data class RoomSettings(
    override val schemaDirectories: Set<String>? = null,
    override val generateKotlin: Boolean? = null,
    val enabled: Boolean = true
) : RoomExtension {

    fun applyTo(extension: androidx.room.gradle.RoomExtension) {
        schemaDirectories?.forEach(extension::schemaDirectory)
        extension::generateKotlin trySet generateKotlin
    }
}
