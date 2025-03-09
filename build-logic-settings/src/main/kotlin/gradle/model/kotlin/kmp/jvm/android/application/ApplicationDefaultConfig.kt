package gradle.model.kotlin.kmp.jvm.android.application

import com.android.build.api.dsl.VariantDimension
import gradle.model.kotlin.kmp.jvm.android.DefaultConfigDsl
import org.gradle.api.Project

/**
 * Specifies defaults for properties that the Android application plugin applies to all build variants.
 *
 * You can override any `defaultConfig` property when
 * [configuring product flavors](https://developer.android.com/studio/build/build-variants.html#product-flavors).
 * See [ApplicationProductFlavor].
 */
internal interface ApplicationDefaultConfig : ApplicationBaseFlavor, DefaultConfigDsl {

    context(Project)
    override fun applyTo(dimension: VariantDimension) {
        super<ApplicationBaseFlavor>.applyTo(dimension)
        super<DefaultConfigDsl>.applyTo(dimension)
    }
}
