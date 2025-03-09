package gradle.model.kotlin.kmp.jvm.android.library

import gradle.model.kotlin.kmp.jvm.android.InternalTestedExtension
import org.gradle.api.Project

/** See [gradle.model.android.InternalCommonExtension] */
internal interface InternalLibraryExtension :
    LibraryExtensionDsl,
    InternalTestedExtension<
        LibraryBuildFeatures,
        LibraryBuildType,
        LibraryDefaultConfig,
        LibraryProductFlavor,
        LibraryAndroidResources,
        LibraryInstallation,
        > {

    context(Project)
    override fun applyTo() {
        super<LibraryExtensionDsl>.applyTo()
        super<InternalTestedExtension>.applyTo()
    }
}
