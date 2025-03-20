package gradle.plugins.android

import com.android.build.api.dsl.DynamicFeatureDefaultConfig
import com.android.build.api.dsl.VariantDimension
import org.gradle.api.Project

/**
 * Specifies defaults for properties that the Android dynamic-feature plugin applies to all build variants.
 *
 * You can override any `defaultConfig` property when
 * [configuring product flavors](https://developer.android.com/studio/build/build-variants.html#product-flavors).
 * See [DynamicFeatureProductFlavor].
 */
internal interface DynamicFeatureDefaultConfig<in T: DynamicFeatureDefaultConfig> :
    DynamicFeatureBaseFlavor<T>,
    DefaultConfigDsl<T> {

    context(Project)
    override fun applyTo(recipient: T) {
        super<DynamicFeatureBaseFlavor>.applyTo(recipient)
        super<DefaultConfigDsl>.applyTo(recipient)
    }
}
