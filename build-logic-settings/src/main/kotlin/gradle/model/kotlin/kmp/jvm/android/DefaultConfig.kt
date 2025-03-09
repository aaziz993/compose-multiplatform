package gradle.model.kotlin.kmp.jvm.android

import com.android.build.api.dsl.VariantDimension
import gradle.model.kotlin.kmp.jvm.android.application.ApplicationDefaultConfig
import gradle.model.kotlin.kmp.jvm.android.library.LibraryDefaultConfig
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
