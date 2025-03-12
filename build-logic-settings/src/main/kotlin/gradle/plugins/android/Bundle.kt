package gradle.plugins.android

import com.android.build.api.dsl.Bundle
import gradle.api.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/**
 * DSL object for configuring the Android Application Bundle
 *
 * This is accessed via [ApplicationExtension.bundle]
 */
@Serializable
internal data class Bundle(
    val abi: BundleAbi? = null,
    val density: BundleDensity? = null,
    val language: BundleLanguage? = null,
    val texture: BundleTexture? = null,
    val deviceTier: BundleDeviceTier? = null,
    val codeTransparency: BundleCodeTransparency? = null,
    val storeArchive: BundleStoreArchive? = null,
    val integrityConfigDir: String? = null,
    val countrySet: BundleCountrySet? = null,
    val aiModelVersion: BundleAiModelVersion? = null,
) {

    context(Project)
    @Suppress("UnstableApiUsage")
    fun applyTo(bundle: Bundle) {
        abi?.applyTo(bundle.abi)
        density?.applyTo(bundle.density)
        language?.applyTo(bundle.language)
        texture?.applyTo(bundle.texture)
        deviceTier?.applyTo(bundle.deviceTier)
        codeTransparency?.applyTo(bundle.codeTransparency)
        storeArchive?.applyTo(bundle.storeArchive)
        bundle.integrityConfigDir tryAssign integrityConfigDir?.let(layout.projectDirectory::dir)
        countrySet?.applyTo(bundle.countrySet)
        aiModelVersion?.applyTo(bundle.aiModelVersion)
    }
}
