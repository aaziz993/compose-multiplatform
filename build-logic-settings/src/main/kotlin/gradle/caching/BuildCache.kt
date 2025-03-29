package gradle.caching

import gradle.api.trySet
import gradle.serialization.serializer.JsonContentPolymorphicSerializer
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.initialization.Settings

/**
 * Configuration object for a build cache.
 *
 * @since 3.5
 */
@Serializable(with = BuildCacheKeyTransformingSerializer::class)
internal interface BuildCache<T : org.gradle.caching.configuration.BuildCache> {

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
        receiver::setEnabled trySet enabled
        receiver::setPush trySet push
    }
}

private object BuildCacheContentPolymorphicSerializer : JsonContentPolymorphicSerializer<BuildCache<*>>(
    BuildCache::class,
)

private object BuildCacheKeyTransformingSerializer : KeyTransformingSerializer<BuildCache<*>>(
    BuildCacheContentPolymorphicSerializer,
    "type",
)
