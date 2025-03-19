package gradle.caching

import gradle.api.isCI
import kotlinx.serialization.Serializable
import org.gradle.api.initialization.Settings
import org.gradle.caching.local.DirectoryBuildCache

@Serializable
internal data class DirectoryBuildCache(
    override val enabled: Boolean? = null,
    override val push: Boolean? = null,
    /**
     * Sets the directory to use to store the build cache.
     *
     * The directory is evaluated as per {@code Project.file(Object)}.
     */
    val directory: String? = null,
) : BuildCache<DirectoryBuildCache> {

    context(Settings)
    @Suppress("UnstableApiUsage")
    override fun applyTo(recipient: DirectoryBuildCache) {
        recipient.isEnabled = !isCI

        super.applyTo(recipient)

        directory?.let(layout.rootDirectory::dir)?.let(recipient::setDirectory)
    }

    context(Settings)
    fun applyTo() = applyTo(buildCache.local)
}
