package plugin.project.android.model

import com.android.build.api.dsl.VariantDimension
import org.gradle.api.Project

/**
 * Specifies defaults for properties that the Android library plugin applies to all build variants.
 *
 * You can override any `defaultConfig` property when
 * [configuring product flavors](https://developer.android.com/studio/build/build-variants.html#product-flavors).
 * See [LibraryProductFlavor].
 */
internal interface LibraryDefaultConfig :
    LibraryBaseFlavor,
    DefaultConfig {

    context(Project)
    override fun applyTo(dimension: VariantDimension) {
        super<LibraryBaseFlavor>.applyTo(dimension)
        super<DefaultConfig>.applyTo(dimension)
    }
}
