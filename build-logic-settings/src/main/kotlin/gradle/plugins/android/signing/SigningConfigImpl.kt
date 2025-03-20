package gradle.plugins.android.signing

import com.android.build.gradle.internal.dsl.SigningConfig
import gradle.api.ProjectNamed
import gradle.plugins.android.signing.InternalSigningConfig
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

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
) : ProjectNamed<SigningConfig>, ApkSigningConfig<SigningConfig>, InternalSigningConfig<SigningConfig> {

    context(Project)
    override fun applyTo(recipient: SigningConfig) {
        super<ApkSigningConfig>.applyTo(recipient)
        super<InternalSigningConfig>.applyTo(recipient)
    }
}

internal object SigningConfigTransformingSerializer : KeyTransformingSerializer<SigningConfigImpl>(
    SigningConfigImpl.serializer(),
    "name",
)
