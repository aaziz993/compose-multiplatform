package gradle.plugins.android.library

import com.android.build.api.dsl.LibraryDefaultConfig
import gradle.plugins.android.defaultconfig.DefaultConfigDsl
import org.gradle.api.Project

/**
 * Specifies defaults for properties that the Android library plugin applies to all build variants.
 *
 * You can override any `defaultConfig` property when
 * [configuring product flavors](https://developer.android.com/studio/build/build-variants.html#product-flavors).
 * See [LibraryProductFlavor].
 */
internal interface LibraryDefaultConfig<T : LibraryDefaultConfig> :
    LibraryBaseFlavor<T>,
    DefaultConfigDsl<T> {

    context(project: Project)
    override fun applyTo(receiver: T) {
        super<LibraryBaseFlavor>.applyTo(receiver)
        super<DefaultConfigDsl>.applyTo(receiver)
    }
}
