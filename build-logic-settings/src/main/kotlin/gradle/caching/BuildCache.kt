package gradle.caching

import gradle.serialization.serializer.JsonPolymorphicSerializer
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.initialization.Settings
import org.gradle.caching.configuration.BuildCacheConfiguration

/**
 * Configuration object for a build cache.
 *
 * @since 3.5
 */
@Serializable(with = BuildCacheSerializer::class)
internal interface BuildCache<T: org.gradle.caching.configuration.BuildCache> {

    /**
     * Sets whether the build cache is enabled.
     */
    val enabled: Boolean?

    /**
     * Sets whether a given build can store outputs in the build cache.
     */
    val push: Boolean?

    context(Settings)
    fun applyTo(receiver: T) {
        enabled?.let(receiver::setEnabled)
        push?.let(receiver::setPush)
    }
}

private object BuildCacheSerializer : JsonPolymorphicSerializer<BuildCache<*>>(
    BuildCache::class,
)

internal object BuildCacheKeyTransformingSerializer : KeyTransformingSerializer<BuildCache<*>>(
    BuildCacheSerializer,
    "type",
)
