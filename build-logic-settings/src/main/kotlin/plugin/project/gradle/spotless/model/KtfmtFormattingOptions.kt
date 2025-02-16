package plugin.project.gradle.spotless.model

import kotlinx.serialization.Serializable

@Serializable
internal data class KtfmtFormattingOptions(
    val maxWidth: Int? = null,
    val blockIndent: Int? = null,
    val continuationIndent: Int? = null,
    val removeUnusedImport: Boolean? = null,
)
