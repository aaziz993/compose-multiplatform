package plugin.project.kotlin.room.model

import kotlinx.serialization.Serializable

@Serializable
internal data class RoomSettings(
    override val schemaDirectories: Set<String>? = null,
    override var generateKotlin: Boolean? = null,
    val enabled: Boolean = true
) : RoomExtension
