package gradle.model.android

import com.android.build.api.dsl.VariantDimension
import org.gradle.api.Project

/**
 * Shared properties between [LibraryProductFlavor] and [LibraryDefaultConfig]
 *
 * See [ProductFlavor] and [DefaultConfig] for more information.
 */
internal interface LibraryBaseFlavor :
    BaseFlavor,
    LibraryVariantDimension {

    context(Project)
    override fun applyTo(dimension: VariantDimension) {
        super<BaseFlavor>.applyTo(dimension)
        super<LibraryVariantDimension>.applyTo(dimension)
    }
}
