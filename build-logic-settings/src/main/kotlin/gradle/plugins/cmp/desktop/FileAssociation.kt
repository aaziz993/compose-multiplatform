package gradle.plugins.cmp.desktop

import kotlinx.serialization.Serializable

@Serializable
internal data class FileAssociation(
    val mimeType: String,
    val extension: String,
    val description: String,
    val iconFile: String? = null
)
