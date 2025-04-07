package gradle.caching

import klib.data.type.reflection.trySet
import klib.data.type.serialization.json.serializer.ReflectionJsonObjectTransformingPolymorphicSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.initialization.Settings

/**
 * Configuration object for a build cache.
 *
 * @since 3.5
 */
@Serializable(with = ReflectionBuildCacheObjectTransformingPolymorphicSerializer::class)
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

private class ReflectionBuildCacheObjectTransformingPolymorphicSerializer(serializer: KSerializer<Nothing>)
    : ReflectionJsonObjectTransformingPolymorphicSerializer<BuildCache<*>>(
    BuildCache::class,
)
