package gradle.model.android.application

import com.android.build.gradle.internal.dsl.InternalApplicationExtension
import gradle.model.android.InternalTestedExtension
import org.gradle.api.Project

/** See [InternalCommonExtension] */
internal interface InternalApplicationExtension :
    ApplicationExtensionDsl,
    InternalTestedExtension<
        ApplicationBuildFeatures,
        ApplicationBuildType,
        ApplicationDefaultConfig,
        ApplicationProductFlavor,
        ApplicationAndroidResources,
        ApplicationInstallation,
        > {

    context(Project)
    fun applyTo(extension: InternalApplicationExtension) {
        super<ApplicationExtensionDsl>.applyTo(extension)
        super<InternalTestedExtension>.applyTo(extension)
    }
}
