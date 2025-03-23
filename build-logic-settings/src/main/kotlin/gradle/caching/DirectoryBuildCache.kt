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
    override fun applyTo(receiver: DirectoryBuildCache) {
        receiver.isEnabled = !isCI

        super.applyTo(receiver)

        directory?.let(layout.rootDirectory::dir)?.let(receiver::setDirectory)
    }

    context(Settings)
    fun applyTo() = applyTo(buildCache.local)
}
