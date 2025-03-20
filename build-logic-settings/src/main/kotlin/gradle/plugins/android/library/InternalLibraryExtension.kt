package gradle.plugins.android.library

import com.android.build.api.dsl.LibraryAndroidResources
import com.android.build.api.dsl.LibraryBuildFeatures
import com.android.build.api.dsl.LibraryBuildType
import com.android.build.api.dsl.LibraryDefaultConfig
import com.android.build.api.dsl.LibraryInstallation
import com.android.build.api.dsl.LibraryProductFlavor
import gradle.plugins.android.InternalTestedExtension
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
