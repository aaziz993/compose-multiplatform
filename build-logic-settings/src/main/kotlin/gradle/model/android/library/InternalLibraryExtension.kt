package gradle.model.android.library

import com.android.build.gradle.internal.dsl.InternalLibraryExtension
import gradle.model.android.InternalTestedExtension
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
    fun applyTo(extension: InternalLibraryExtension) {
        super<LibraryExtensionDsl>.applyTo(extension)
        super<InternalTestedExtension>.applyTo(extension)
    }
}
