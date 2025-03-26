package gradle.caching

import gradle.api.isCI
import org.gradle.api.initialization.Settings
import org.gradle.caching.configuration.AbstractBuildCache

public abstract class AbstractBuildCache<T : AbstractBuildCache> : BuildCache<T> {

    context(Settings)
    override fun applyTo(receiver: T) {
        // better set it to true only for CI builds.
        receiver.isPush = isCI

        super.applyTo(receiver)
    }
}
