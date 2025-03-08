package gradle.model.android.application

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
    override fun applyTo() {
        super<ApplicationExtensionDsl>.applyTo()
        super<InternalTestedExtension>.applyTo()
    }
}
