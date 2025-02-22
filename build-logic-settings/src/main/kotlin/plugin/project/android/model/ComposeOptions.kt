package plugin.project.android.model

import kotlinx.serialization.Serializable

/**
 * Optional settings for the Compose feature.
 */
@Serializable
internal data class ComposeOptions(
    /**
     * Sets the version of the Kotlin Compiler extension for the project or null if using
     * the default one.
     */
    val kotlinCompilerExtensionVersion: String? = null,
)
