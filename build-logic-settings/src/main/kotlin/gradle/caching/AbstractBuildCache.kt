package gradle.caching

import gradle.api.isCI
import org.gradle.api.initialization.Settings

public abstract class AbstractBuildCache : BuildCache {

    context(Settings)
    override fun applyTo(cache: org.gradle.caching.configuration.BuildCache) {
        // better set it to true only for CI builds.
        cache.isPush = isCI

        super.applyTo(cache)
    }
}
