package gradle.plugins.android.signing

import com.android.build.gradle.internal.dsl.SigningConfig
import gradle.api.NamedKeyTransformingSerializer
import gradle.api.ProjectNamed
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class SigningConfigImpl(
    override val name: String? = null,
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
    override fun applyTo(receiver: SigningConfig) {
        super<ApkSigningConfig>.applyTo(receiver)
        super<InternalSigningConfig>.applyTo(receiver)
    }
}

internal object SigningConfigKeyTransformingSerializer : NamedKeyTransformingSerializer<SigningConfigImpl>(
    SigningConfigImpl.serializer(),
)
