package gradle.plugins.android.test

import com.android.build.api.dsl.TestVariantDimension
import gradle.accessors.android
import gradle.api.trySet
import gradle.plugins.android.VariantDimension
import org.gradle.api.Project

/**
 * Shared properties between DSL objects that contribute to a separate-test-project variant.
 *
 * That is, [TestBuildType] and [TestProductFlavor] and [TestDefaultConfig].
 */
internal interface TestVariantDimension<T : TestVariantDimension> :
    VariantDimension<T> {

    /**
     * Returns whether multi-dex is enabled.
     *
     * This can be null if the flag is not set, in which case the default value is used.
     */
    val multiDexEnabled: Boolean?

    /** The associated signing config or null if none are set on the variant dimension. */
    val signingConfig: String?

    context(project: Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        receiver::multiDexEnabled trySet multiDexEnabled
        receiver::signingConfig trySet signingConfig?.let(project.android.signingConfigs::getByName)
    }
}
