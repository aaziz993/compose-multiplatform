package gradle.caching

import gradle.api.isCI
import org.gradle.api.initialization.Settings
import org.gradle.caching.configuration.AbstractBuildCache

public abstract class AbstractBuildCache<in T: AbstractBuildCache> : BuildCache<T> {

    context(Settings)
    override fun applyTo(recipient: T) {
        // better set it to true only for CI builds.
        recipient.isPush = isCI

        super.applyTo(recipient)
    }
}
