package gradle.plugins.android.application.bundle

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
    fun applyTo(receiver: Bundle) {
        abi?.applyTo(receiver.abi)
        density?.applyTo(receiver.density)
        language?.applyTo(receiver.language)
        texture?.applyTo(receiver.texture)
        deviceTier?.applyTo(receiver.deviceTier)
        codeTransparency?.applyTo(receiver.codeTransparency)
        storeArchive?.applyTo(receiver.storeArchive)
        receiver.integrityConfigDir tryAssign integrityConfigDir?.let(layout.projectDirectory::dir)
        countrySet?.applyTo(receiver.countrySet)
        aiModelVersion?.applyTo(receiver.aiModelVersion)
    }
}
