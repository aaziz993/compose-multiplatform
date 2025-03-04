package plugin.project.android.model

import kotlinx.serialization.Serializable

/** DSL object to configure signing configs. */
@Serializable
internal data class DefaultSigningConfig(
    override val storeFile: String? = null,
    override val storePassword: String? = null,
    override val keyAlias: String? = null,
    override val keyPassword: String? = null,
    override val storeType: String? = null,
    override val initWith: String? = null,
    override val enableV1Signing: Boolean? = null,
    override val enableV2Signing: Boolean? = null,
    override val enableV3Signing: Boolean? = null,
    override val enableV4Signing: Boolean? = null,
    override val name: String
) : ApkSigningConfig
