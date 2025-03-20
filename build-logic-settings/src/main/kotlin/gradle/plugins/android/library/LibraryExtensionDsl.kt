package gradle.plugins.android.library

import com.android.build.api.dsl.LibraryExtension
import gradle.accessors.android
import gradle.api.applyTo
import gradle.plugins.android.CommonExtension
import gradle.plugins.android.Prefab
import gradle.plugins.android.PrivacySandbox
import gradle.plugins.android.TestedExtensionDsl
import org.gradle.api.Project

/**
 * Extension for the Android Library Gradle Plugin.
 *
 * This is the `android` block when the `com.android.library` plugin is applied.
 *
 * Only the Android Gradle Plugin should create instances of interfaces in com.android.build.api.dsl.
 */
internal interface LibraryExtensionDsl :
    CommonExtension<
        LibraryBuildFeatures,
        LibraryBuildType,
        LibraryDefaultConfigImpl,
        LibraryProductFlavorImpl,
        LibraryAndroidResources,
        LibraryInstallation,
        >, TestedExtensionDsl {
    // TODO(b/140406102)

    /** Aidl files to package in the aar. */
    val aidlPackagedList: Set<String>?
    val setAidlPackagedList: Set<String>?

    /**
     * container of Prefab options
     */
    val prefab: Set<Prefab>?

    /**
     * Customizes publishing build variant artifacts from library module to a Maven repository.
     *
     * For more information about the properties you can configure in this block, see [LibraryPublishing]
     */
    val publishing: LibraryPublishing?

    val privacySandbox: PrivacySandbox?

    context(Project)
    @Suppress("UnstableApiUsage")
    override fun applyTo() {
        super<CommonExtension>.applyTo()
        super<TestedExtensionDsl>.applyTo()

        val extension = android as LibraryExtension

        aidlPackagedList?.let { aidlPackagedList ->
            extension.aidlPackagedList?.addAll(aidlPackagedList)

        }

        setAidlPackagedList?.let { setAidlPackagedList ->
            extension.aidlPackagedList?.also(MutableCollection<*>::clear)?.addAll(setAidlPackagedList)

        }

        prefab?.forEach { prefab ->
            prefab.applyTo(extension.prefab)
        }

        publishing?.applyTo(extension.publishing)

        privacySandbox?.applyTo(extension.privacySandbox)
    }
}
