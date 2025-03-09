package gradle.model.kotlin.kmp.jvm.android

import org.gradle.api.Project

/**
 * Provides test components that are common to [AppExtension], [gradle.model.android.library.LibraryExtension], and
 * [FeatureExtension].
 *
 * To learn more about testing Android projects, read
 * [Test your app](https://developer.android.com/studio/test/index.html)
 */
internal abstract class TestedExtension : BaseExtension, TestedExtensionDsl {

    context(Project)
    override fun applyTo() {
        super<BaseExtension>.applyTo()
        super<TestedExtensionDsl>.applyTo()
    }
}
