package gradle.caching

import gradle.caching.remote.DevelocityBuildCache
import gradle.caching.remote.HttpBuildCache
import kotlinx.serialization.Serializable
import org.gradle.api.initialization.Settings
import org.gradle.caching.configuration.BuildCacheConfiguration
import org.gradle.kotlin.dsl.develocity
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
    val remotes: LinkedHashSet<@Serializable(with = BuildCacheKeyTransformingSerializer::class) BuildCache<out org.gradle.caching.configuration.BuildCache>>? = null,
) {

    context(Settings)
    @Suppress("UNCHECKED_CAST")
    fun applyTo(receiver: BuildCacheConfiguration) {
        local?.applyTo()

        remotes?.forEach { remote ->
            when (remote) {
                is HttpBuildCache -> receiver.remote(org.gradle.caching.http.HttpBuildCache::class.java) {
                    remote.applyTo(this)
                }

                is DevelocityBuildCache -> receiver.remote(settings.develocity.buildCache) {
                    remote.applyTo(this)
                }

                else -> error("Unsupported remote build cache type: ${remote::class.java.name}")
            }
        }
    }
}


