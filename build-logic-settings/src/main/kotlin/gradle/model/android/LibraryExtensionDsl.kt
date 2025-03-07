package gradle.model.android

import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.TestedExtension
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
        LibraryDefaultConfig,
        LibraryProductFlavor,
        LibraryAndroidResources,
        LibraryInstallation,
        >,
    TestedExtensionDsl {
    // TODO(b/140406102)

    /** Aidl files to package in the aar. */
    val aidlPackagedList: List<String>?

    /**
     * container of Prefab options
     */
    val prefab: List<Prefab>?

    /**
     * Customizes publishing build variant artifacts from library module to a Maven repository.
     *
     * For more information about the properties you can configure in this block, see [LibraryPublishing]
     */
    val publishing: LibraryPublishing?

    val privacySandbox: PrivacySandbox?

    context(Project)
    @Suppress("UnstableApiUsage")
    override fun applyTo(extension: com.android.build.api.dsl.CommonExtension<*, *, *, *, *, *>) {
        super<CommonExtension>.applyTo(extension)

        super<TestedExtensionDsl>.applyTo(extension as TestedExtension)

        extension as LibraryExtension

        aidlPackagedList?.let { aidlPackagedList ->
            extension.aidlPackagedList?.addAll(aidlPackagedList)

        }

        prefab?.forEach { prefab ->
            prefab.name?.also { name -> extension.prefab.findByName(name)?.apply(prefab::applyTo) }
                ?: extension.prefab.all(prefab::applyTo)
        }

        publishing?.applyTo(extension.publishing)

        privacySandbox?.applyTo(extension.privacySandbox)
    }
}
