package gradle.plugins.android

import gradle.plugins.android.application.ApplicationProductFlavor
import gradle.plugins.android.library.LibraryProductFlavor
import org.gradle.api.Project

internal interface ProductFlavor<T : com.android.build.gradle.internal.dsl.ProductFlavor> : ApplicationProductFlavor<T>,
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
