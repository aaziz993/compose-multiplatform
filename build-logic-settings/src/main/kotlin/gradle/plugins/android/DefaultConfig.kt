package gradle.plugins.android

import com.android.build.api.dsl.VariantDimension
import gradle.plugins.android.application.ApplicationDefaultConfig
import gradle.plugins.android.library.LibraryDefaultConfig
import kotlinx.serialization.Serializable
import org.gradle.api.Project

internal abstract class DefaultConfig : ApplicationDefaultConfig,
    DynamicFeatureDefaultConfig,
    LibraryDefaultConfig,
    TestDefaultConfig {

    context(Project)
    override fun applyTo(dimension: VariantDimension) {
        super<ApplicationDefaultConfig>.applyTo(dimension)
        super<DynamicFeatureDefaultConfig>.applyTo(dimension)
        super<LibraryDefaultConfig>.applyTo(dimension)
        super<TestDefaultConfig>.applyTo(dimension)
    }
}
