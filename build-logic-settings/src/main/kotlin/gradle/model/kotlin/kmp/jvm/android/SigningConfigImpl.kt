package gradle.model.kotlin.kmp.jvm.android

import gradle.model.Named
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class SigningConfigImpl(
    override val name: String="",
    override val enableV1Signing: Boolean? = null,
    override val enableV2Signing: Boolean? = null,
    override val enableV3Signing: Boolean? = null,
    override val enableV4Signing: Boolean? = null,
    override val storeFile: String? = null,
    override val storePassword: String? = null,
    override val keyAlias: String? = null,
    override val keyPassword: String? = null,
    override val storeType: String? = null,
    override val initWith: String? = null,
    override val isSigningReady: Boolean
) : Named, ApkSigningConfig, InternalSigningConfig {

    context(Project)
    override fun applyTo(named: org.gradle.api.Named) {
        super<ApkSigningConfig>.applyTo(named)
        super<InternalSigningConfig>.applyTo(named)
    }
}
