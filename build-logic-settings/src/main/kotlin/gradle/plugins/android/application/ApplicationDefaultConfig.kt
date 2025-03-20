package gradle.plugins.android.application

import com.android.build.api.dsl.ApplicationDefaultConfig
import com.android.build.api.dsl.VariantDimension
import gradle.plugins.android.DefaultConfigDsl
import org.gradle.api.Project

/**
 * Specifies defaults for properties that the Android application plugin applies to all build variants.
 *
 * You can override any `defaultConfig` property when
 * [configuring product flavors](https://developer.android.com/studio/build/build-variants.html#product-flavors).
 * See [ApplicationProductFlavor].
 */
internal interface ApplicationDefaultConfig< T : ApplicationDefaultConfig> : ApplicationBaseFlavor<T>, DefaultConfigDsl<T> {

    context(Project)
    override fun applyTo(recipient: T) {
        super<ApplicationBaseFlavor>.applyTo(recipient)
        super<DefaultConfigDsl>.applyTo(recipient)
    }
}
