package gradle.model.android;

import com.android.build.api.dsl.TestedExtension
import org.gradle.api.Project

/**
 * Provides test components that are common to [AppExtension], [LibraryExtension], and
 * [FeatureExtension].
 *
 * To learn more about testing Android projects, read
 * [Test your app](https://developer.android.com/studio/test/index.html)
 */
internal abstract class TestedExtension : BaseExtension, TestedExtensionDsl {

    context(Project)
    override fun applyTo(extension: TestedExtension) {
        super<TestedExtensionDsl>.applyTo(extension)

        extension as com.android.build.gradle.TestedExtension

        super<BaseExtension>.applyTo(extension)
    }
}
