package gradle.plugins.android.application

import com.android.build.api.dsl.ApplicationAndroidResources
import com.android.build.api.dsl.ApplicationBuildFeatures
import com.android.build.api.dsl.ApplicationBuildType
import com.android.build.api.dsl.ApplicationDefaultConfig
import com.android.build.api.dsl.ApplicationInstallation
import com.android.build.api.dsl.ApplicationProductFlavor
import gradle.plugins.android.InternalTestedExtension
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
