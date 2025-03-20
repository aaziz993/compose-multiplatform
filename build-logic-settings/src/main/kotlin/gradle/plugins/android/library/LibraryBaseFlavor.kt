package gradle.plugins.android.library

import com.android.build.api.dsl.LibraryBaseFlavor
import gradle.plugins.android.flavor.BaseFlavor
import org.gradle.api.Project

/**
 * Shared properties between [LibraryProductFlavor] and [LibraryDefaultConfig]
 *
 * See [ProductFlavorDsl] and [DefaultConfigDsl] for more information.
 */
internal interface LibraryBaseFlavor<T: LibraryBaseFlavor> : BaseFlavor<T>, LibraryVariantDimension<T> {

    context(Project)
    override fun applyTo(recipient: T) {
        super<BaseFlavor>.applyTo(recipient)
        super<LibraryVariantDimension>.applyTo(recipient)
    }
}
