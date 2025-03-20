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
    override fun applyTo(recipient: T) {
        super<ApplicationProductFlavor>.applyTo(recipient)
        super<DynamicFeatureProductFlavor>.applyTo(recipient)
        super<LibraryProductFlavor>.applyTo(recipient)
        super<TestProductFlavor>.applyTo(recipient)
    }
}
