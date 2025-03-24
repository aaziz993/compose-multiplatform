package gradle.plugins.android.application

import gradle.plugins.android.defaultconfig.DefaultConfigDsl
import org.gradle.api.Project

/**
 * Specifies defaults for properties that the Android application plugin applies to all build variants.
 *
 * You can override any `defaultConfig` property when
 * [configuring product flavors](https://developer.android.com/studio/build/build-variants.html#product-flavors).
 * See [ApplicationProductFlavor].
 */
internal interface ApplicationDefaultConfig<T : com.android.build.api.dsl.ApplicationDefaultConfig>
    : ApplicationBaseFlavor<T>, DefaultConfigDsl<T> {

    context(Project)
    override fun applyTo(receiver: T) {
        super<ApplicationBaseFlavor>.applyTo(receiver)
        super<DefaultConfigDsl>.applyTo(receiver)
    }
}
