package gradle.plugins.android.test

import com.android.build.api.dsl.TestProductFlavor
import gradle.plugins.android.flavor.ProductFlavorDsl
import gradle.plugins.android.flavor.TestBaseFlavor
import org.gradle.api.Project

/**
 * Encapsulates all product flavors properties for test projects.
 *
 * Test projects have a target application project that they depend on and flavor matching works in
 * the same way as library dependencies. Therefore, to test multiple flavors of an application,
 * you can declare corresponding product flavors here. If you want to use some, you can use
 * [missingDimensionStrategy] to resolve any conflicts.
 *
 * See [gradle.model.android.application.ApplicationProductFlavor]
 */
internal interface TestProductFlavor<T : TestProductFlavor> :
    TestBaseFlavor<T>,
    ProductFlavorDsl<T> {

    context(Project)
    override fun applyTo(receiver: T) {
        super<TestBaseFlavor>.applyTo(receiver)
        super<ProductFlavorDsl>.applyTo(receiver)
    }
}
