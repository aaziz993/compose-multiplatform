package gradle.plugins.android.library

import com.android.build.api.dsl.LibraryDefaultConfig
import com.android.build.api.dsl.VariantDimension
import gradle.plugins.android.DefaultConfigDsl
import org.gradle.api.Project

/**
 * Specifies defaults for properties that the Android library plugin applies to all build variants.
 *
 * You can override any `defaultConfig` property when
 * [configuring product flavors](https://developer.android.com/studio/build/build-variants.html#product-flavors).
 * See [LibraryProductFlavor].
 */
internal interface LibraryDefaultConfig<in T : LibraryDefaultConfig> :
    LibraryBaseFlavor<T>,
    DefaultConfigDsl<T> {

    context(Project)
    override fun applyTo(recipient: T) {
        super<LibraryBaseFlavor>.applyTo(recipient)
        super<DefaultConfigDsl>.applyTo(recipient)
    }
}
