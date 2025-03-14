package gradle.caching

import kotlinx.serialization.Serializable
import org.gradle.api.initialization.Settings
import org.gradle.caching.configuration.BuildCacheConfiguration
import org.jetbrains.dokka.plugability.configuration

/**
 * Configuration for the [build cache](https://docs.gradle.org/current/userguide/build_cache.html) for an entire Gradle build.
 *
 * @since 3.5
 */
@Serializable
internal data class BuildCacheConfiguration(
    /**
     * Executes the given action against the local configuration.
     *
     * @param configuration the action to execute against the local cache configuration.
     */
    val local: DirectoryBuildCache? = null,
    /**
     * Configures a remote cache with the given type.
     *
     *
     * If a remote build cache has already been configured with a different type, this method replaces it.
     *
     *
     *
     * Storing ("push") in the remote build cache is disabled by default.
     *
     * @param type the type of remote cache to configure.
     */
    val remotes: List<@Serializable(with = BuildCacheTransformingSerializer::class) BuildCache>? = null,
) {

    context(Settings)
    @Suppress("UNCHECKED_CAST")
    fun applyTo(configuration: BuildCacheConfiguration) {
        local?.applyTo()

        remotes?.forEach { remote ->
            remote.applyTo(configuration)
        }
    }
}


