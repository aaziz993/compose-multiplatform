package gradle.model.android

import com.android.build.api.dsl.VariantDimension
import gradle.model.android.application.ApplicationProductFlavor
import gradle.model.android.library.LibraryProductFlavor
import org.gradle.api.Project

internal interface ProductFlavor : ApplicationProductFlavor,
    DynamicFeatureProductFlavor,
    LibraryProductFlavor,
    TestProductFlavor {

    context(Project)
    override fun applyTo(dimension: VariantDimension) {
        super<ApplicationProductFlavor>.applyTo(dimension)
        super<DynamicFeatureProductFlavor>.applyTo(dimension)
        super<LibraryProductFlavor>.applyTo(dimension)
        super<TestProductFlavor>.applyTo(dimension)
    }
}
