package plugin.settings.model

import kotlinx.serialization.Serializable
import org.gradle.api.initialization.Settings
import org.gradle.caching.local.DirectoryBuildCache
import plugin.project.gradle.model.BuildCache

@Serializable
internal data class DirectoryBuildCache(
    override val isEnabled: Boolean? = null,
    override val isPush: Boolean? = null,
    /**
     * Sets the directory to use to store the build cache.
     *
     * The directory is evaluated as per {@code Project.file(Object)}.
     */
    val directory: String? = null,
) : BuildCache {

    context(Settings)
    @Suppress("UnstableApiUsage")
    fun applyTo(cache: DirectoryBuildCache) {
        super.applyTo(cache)
        directory?.let(layout.rootDirectory::dir)?.let(cache::setDirectory)
    }
}
