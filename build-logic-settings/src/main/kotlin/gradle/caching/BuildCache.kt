package gradle.caching

import gradle.serialization.serializer.JsonContentPolymorphicSerializer
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import org.gradle.caching.configuration.BuildCacheConfiguration

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

private object BuildCacheSerializer : JsonContentPolymorphicSerializer<BuildCache>(BuildCache::class) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<BuildCache> = when {
        element.jsonObject.containsKey("url") -> HttpBuildCache.serializer()
        else -> throw IllegalArgumentException("Unknown json value: $element")
    }
}
