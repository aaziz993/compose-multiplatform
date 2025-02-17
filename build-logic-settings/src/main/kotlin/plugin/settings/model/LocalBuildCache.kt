package plugin.settings.model

import kotlinx.serialization.Serializable

@Serializable
internal data class LocalBuildCache(
    override val isEnabled: Boolean? = null,
    override val isPush: Boolean? = null,
    /**
     * Sets the directory to use to store the build cache.
     *
     * The directory is evaluated as per {@code Project.file(Object)}.
     */
    val directory: String? = null,
) : BuildCache
