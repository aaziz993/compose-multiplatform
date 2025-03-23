package gradle.plugins.android.flavor

import com.android.build.gradle.internal.dsl.ProductFlavor
import gradle.plugins.android.application.ApplicationProductFlavor
import gradle.plugins.android.library.LibraryProductFlavor
import gradle.plugins.android.test.TestProductFlavor
import org.gradle.api.Project

internal interface ProductFlavor<T : ProductFlavor> : ApplicationProductFlavor<T>,
    DynamicFeatureProductFlavor<T>,
    LibraryProductFlavor<T>,
    TestProductFlavor<T> {

    context(Project)
    override fun applyTo(receiver: T) {
        super<ApplicationProductFlavor>.applyTo(receiver)
        super<DynamicFeatureProductFlavor>.applyTo(receiver)
        super<LibraryProductFlavor>.applyTo(receiver)
        super<TestProductFlavor>.applyTo(receiver)
    }
}
