package gradle.model.android

import com.android.build.api.dsl.VariantDimension
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
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
