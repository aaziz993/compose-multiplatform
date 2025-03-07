package gradle.model.android

import com.android.build.api.dsl.LibraryPublishing

/** See [InternalCommonExtension] */
internal interface InternalLibraryExtension :
    LibraryExtensionDsl,
    InternalTestedExtension<
        LibraryBuildFeatures,
        LibraryBuildType,
        LibraryDefaultConfig,
        LibraryProductFlavor,
        LibraryAndroidResources,
        LibraryInstallation,
        >
