package gradle.model.android;

import com.android.build.gradle.TestedExtension
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
    fun applyTo(extension: TestedExtension) {
        super<BaseExtension>.applyTo(extension)
        super<TestedExtensionDsl>.applyTo(extension)
    }
}
