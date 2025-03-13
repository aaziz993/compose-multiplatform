package gradle.caching

import gradle.serialization.serializer.JsonPolymorphicSerializer
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable

/**
 * Configuration object for a build cache.
 *
 * @since 3.5
 */
@Serializable(with = BuildCacheSerializer::class)
internal interface BuildCache {

    val type: Class<out org.gradle.caching.configuration.BuildCache>
        get() = throw UnsupportedOperationException()

    /**
     * Sets whether the build cache is enabled.
     */
    val isEnabled: Boolean?

    /**
     * Sets whether a given build can store outputs in the build cache.
     */
    val isPush: Boolean?

    fun applyTo(cache: org.gradle.caching.configuration.BuildCache) {
        isEnabled?.let(cache::setEnabled)
        isPush?.let(cache::setPush)
    }
}

private object BuildCacheSerializer : JsonPolymorphicSerializer<BuildCache>(
    BuildCache::class,
)

internal object BuildCacheTransformingSerializer : KeyTransformingSerializer<BuildCache>(
    BuildCache.serializer(),
    "type",
)
