package gradle.model.kotlin.kmp.jvm.android

import com.android.build.api.dsl.VariantDimension
import org.gradle.api.Project

/**
 * Specifies defaults for properties that the Android test plugin applies to all build variants.
 *
 * You can override any `defaultConfig` property when
 * [configuring product flavors](https://developer.android.com/studio/build/build-variants.html#product-flavors).
 * See [TestProductFlavor].
 */
internal interface TestDefaultConfig :
    TestBaseFlavor,
    DefaultConfigDsl {

    context(Project)
    override fun applyTo(dimension: VariantDimension) {
        super<TestBaseFlavor>.applyTo(dimension)
        super<DefaultConfigDsl>.applyTo(dimension)
    }
}
