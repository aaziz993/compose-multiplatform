package gradle.plugins.android.application

import gradle.plugins.android.InternalTestedExtension
import org.gradle.api.Project

/** See [InternalCommonExtension] */
internal interface InternalApplicationExtension :
    ApplicationExtensionDsl,
    InternalTestedExtension<
        ApplicationBuildFeatures,
        ApplicationBuildType,
        ApplicationDefaultConfigImpl,
        ApplicationProductFlavorImpl,
        ApplicationAndroidResources,
        ApplicationInstallation,
        > {

    context(Project)
    override fun applyTo() {
        super<ApplicationExtensionDsl>.applyTo()
        super<InternalTestedExtension>.applyTo()
    }
}
