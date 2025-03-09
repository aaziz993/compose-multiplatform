package gradle.model.android

import gradle.model.kotlin.kmp.KotlinTarget
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable

@Serializable
internal data class ApkSigningConfigImpl(
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
    override val name: String = "",
) : ApkSigningConfig

internal object ApkSigningConfigTransformingSerializer : KeyTransformingSerializer<ApkSigningConfigImpl>(
    ApkSigningConfigImpl.serializer(),
    "name",
)
