package gradle.plugins.android.defaultconfig

import com.android.build.api.dsl.DynamicFeatureDefaultConfig
import gradle.plugins.android.flavor.DynamicFeatureBaseFlavor
import org.gradle.api.Project

/**
 * Specifies defaults for properties that the Android dynamic-feature plugin applies to all build variants.
 *
 * You can override any `defaultConfig` property when
 * [configuring product flavors](https://developer.android.com/studio/build/build-variants.html#product-flavors).
 * See [gradle.plugins.android.flavor.DynamicFeatureProductFlavor].
 */
internal interface DynamicFeatureDefaultConfig<T : DynamicFeatureDefaultConfig> :
    DynamicFeatureBaseFlavor<T>,
    DefaultConfigDsl<T> {

    context(Project)
    override fun applyTo(receiver: T) {
        super<DynamicFeatureBaseFlavor>.applyTo(receiver)
        super<DefaultConfigDsl>.applyTo(receiver)
    }
}
