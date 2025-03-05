package plugin.project.android.model

import com.android.build.api.dsl.TestProductFlavor
import com.android.build.api.dsl.VariantDimension
import org.gradle.api.Named
import org.gradle.api.Project

/**
 * Encapsulates all product flavors properties for test projects.
 *
 * Test projects have a target application project that they depend on and flavor matching works in
 * the same way as library dependencies. Therefore, to test multiple flavors of an application,
 * you can declare corresponding product flavors here. If you want to use some, you can use
 * [missingDimensionStrategy] to resolve any conflicts.
 *
 * See [ApplicationProductFlavor]
 */
internal interface TestProductFlavor :
    TestBaseFlavor,
    ProductFlavor {

    context(Project)
    override fun applyTo(named: Named) {
        super<ProductFlavor>.applyTo(named)

        named as TestProductFlavor

        super<TestBaseFlavor>.applyTo(named)
    }
}
