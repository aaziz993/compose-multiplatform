package gradle.caching

import gradle.api.ci.CI
import klib.data.type.reflection.trySet
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
        receiver.isEnabled = !CI.present

        super.applyTo(receiver)

        receiver::setDirectory trySet directory?.let(settings.layout.rootDirectory::dir)
    }

    context(Settings)
    fun applyTo() = applyTo(settings.buildCache.local)
}
