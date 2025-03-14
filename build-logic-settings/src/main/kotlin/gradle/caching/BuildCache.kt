package gradle.caching

import gradle.serialization.serializer.JsonPolymorphicSerializer
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.gradle.caching.configuration.BuildCacheConfiguration

/**
 * Configuration object for a build cache.
 *
 * @since 3.5
 */
@Serializable(with = BuildCacheSerializer::class)
internal interface BuildCache {

    /**
     * Sets whether the build cache is enabled.
     */
    val isEnabled: Boolean?

    /**
     * Sets whether a given build can store outputs in the build cache.
     */
    val isPush: Boolean?

    context(Settings)
    fun applyTo(cache: org.gradle.caching.configuration.BuildCache) {
        isEnabled?.let(cache::setEnabled)
        isPush?.let(cache::setPush)
    }

    context(Settings)
    fun applyTo(configuration: BuildCacheConfiguration) {
        throw UnsupportedOperationException()
    }
}

private object BuildCacheSerializer : JsonPolymorphicSerializer<BuildCache>(
    BuildCache::class,
)

internal object BuildCacheTransformingSerializer : KeyTransformingSerializer<BuildCache>(
    BuildCache.serializer(),
    "type",
)
