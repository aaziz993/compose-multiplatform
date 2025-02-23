package plugin.project.compose.desktop.model

import kotlinx.serialization.Serializable

@Serializable
internal data class FileAssociation(
    val mimeType: String,
    val extension: String,
    val description: String,
    val iconFile: String? = null
)
