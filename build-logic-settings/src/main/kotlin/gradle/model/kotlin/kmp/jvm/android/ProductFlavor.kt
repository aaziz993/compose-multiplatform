package gradle.model.kotlin.kmp.jvm.android

import com.android.build.api.dsl.VariantDimension
import gradle.model.kotlin.kmp.jvm.android.application.ApplicationProductFlavor
import gradle.model.kotlin.kmp.jvm.android.library.LibraryProductFlavor
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
