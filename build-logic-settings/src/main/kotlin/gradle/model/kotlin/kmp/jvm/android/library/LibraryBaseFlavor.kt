package gradle.model.kotlin.kmp.jvm.android.library

import com.android.build.api.dsl.VariantDimension
import gradle.model.kotlin.kmp.jvm.android.BaseFlavor
import org.gradle.api.Project

/**
 * Shared properties between [LibraryProductFlavor] and [LibraryDefaultConfig]
 *
 * See [ProductFlavorDsl] and [DefaultConfigDsl] for more information.
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
