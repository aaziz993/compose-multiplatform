package gradle.plugins.android.defaultconfig

import com.android.build.gradle.internal.dsl.DefaultConfig
import gradle.plugins.android.defaultconfig.DynamicFeatureDefaultConfig
import gradle.plugins.android.application.ApplicationDefaultConfig
import gradle.plugins.android.library.LibraryDefaultConfig
import gradle.plugins.android.test.TestDefaultConfig
import org.gradle.api.Project

internal abstract class DefaultConfig<T: DefaultConfig> : ApplicationDefaultConfig<T>,
    DynamicFeatureDefaultConfig<T>,
    LibraryDefaultConfig<T>,
    TestDefaultConfig<T> {

    context(Project)
    override fun applyTo(recipient: T) {
        super<ApplicationDefaultConfig>.applyTo(recipient)
        super<DynamicFeatureDefaultConfig>.applyTo(recipient)
        super<LibraryDefaultConfig>.applyTo(recipient)
        super<TestDefaultConfig>.applyTo(recipient)
    }
}
