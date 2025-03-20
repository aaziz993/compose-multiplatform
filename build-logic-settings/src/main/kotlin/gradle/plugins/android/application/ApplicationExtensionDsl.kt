package gradle.plugins.android.application

import com.android.build.api.dsl.ApplicationExtension
import gradle.accessors.android
import gradle.collection.act
import gradle.plugins.android.Bundle
import gradle.plugins.android.CommonExtension
import gradle.plugins.android.DependenciesInfo
import gradle.plugins.android.PrivacySandbox
import gradle.plugins.android.TestedExtensionDsl
import org.gradle.api.Project

/**
 * Extension for the Android Gradle Plugin Application plugin.
 *
 * This is the `android` block when the `com.android.application` plugin is applied.
 *
 * Only the Android Gradle Plugin should create instances of interfaces in com.android.build.api.dsl.
 */
internal interface ApplicationExtensionDsl :
    CommonExtension<
        ApplicationBuildFeatures,
        ApplicationBuildType,
        ApplicationDefaultConfigImpl,
        ApplicationProductFlavorImpl,
        ApplicationAndroidResources,
        ApplicationInstallation,
        >,
    ApkExtension,
    TestedExtensionDsl {
    // TODO(b/140406102)

    /** Specify whether to include SDK dependency information in APKs and Bundles. */
    val dependenciesInfo: DependenciesInfo?

    val bundle: Bundle?

    val dynamicFeatures: Set<String>?
    val setDynamicFeatures: Set<String>?

    /**
     * Set of asset pack subprojects to be included in the app's bundle.
     */
    val assetPacks: Set<String>?
    val setAssetPacks: Set<String>?

    /**
     * Customizes publishing build variant artifacts from app module to a Maven repository.
     *
     * For more information about the properties you can configure in this block, see [ApplicationPublishing]
     */
    val publishing: ApplicationPublishing?

    /** Options related to the consumption of privacy sandbox libraries */
    val privacySandbox: PrivacySandbox?

    context(Project)
    override fun applyTo() {
        val extension = android as ApplicationExtension

        dependenciesInfo?.applyTo(extension.dependenciesInfo)
        bundle?.applyTo(extension.bundle)
        dynamicFeatures?.let(extension.dynamicFeatures::addAll)
        setDynamicFeatures?.act(extension.dynamicFeatures::clear)?.let(extension.dynamicFeatures::addAll)
        assetPacks?.let(extension.assetPacks::addAll)
        setAssetPacks?.act(extension.assetPacks::clear)?.let(extension.assetPacks::addAll)
        publishing?.applyTo(extension.publishing)
        privacySandbox?.applyTo(extension.privacySandbox)
    }
}
