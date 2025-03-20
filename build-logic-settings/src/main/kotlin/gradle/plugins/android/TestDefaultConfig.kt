package gradle.plugins.android

import com.android.build.api.dsl.TestDefaultConfig
import com.android.build.api.dsl.VariantDimension
import org.gradle.api.Project

/**
 * Specifies defaults for properties that the Android test plugin applies to all build variants.
 *
 * You can override any `defaultConfig` property when
 * [configuring product flavors](https://developer.android.com/studio/build/build-variants.html#product-flavors).
 * See [TestProductFlavor].
 */
internal interface TestDefaultConfig<T : TestDefaultConfig> :
    TestBaseFlavor<T>,
    DefaultConfigDsl<T> {

    context(Project)
    override fun applyTo(recipient: T) {
        super<TestBaseFlavor>.applyTo(recipient)
        super<DefaultConfigDsl>.applyTo(recipient)
    }
}
