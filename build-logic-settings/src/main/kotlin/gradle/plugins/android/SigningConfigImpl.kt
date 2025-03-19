package gradle.plugins.android

import gradle.api.BaseNamed
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable

@Serializable
internal data class SigningConfigImpl(
    override val name: String = "",
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
) : BaseNamed, ApkSigningConfig, InternalSigningConfig {

        context(Project)
    override fun applyTo(named: T) {
        super<ApkSigningConfig>.applyTo(named)
        super<InternalSigningConfig>.applyTo(named)
    }
}

internal object SigningConfigTransformingSerializer : KeyTransformingSerializer<SigningConfigImpl>(
    SigningConfigImpl.serializer(),
    "name",
)
