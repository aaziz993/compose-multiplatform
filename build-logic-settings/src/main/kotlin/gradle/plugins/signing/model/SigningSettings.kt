package gradle.plugins.signing.model

import gradle.api.EnabledSettings
import gradle.plugins.signing.InMemoryPgpKeys
import gradle.plugins.signing.SignContentPolymorphicSerializer
import gradle.plugins.signing.SigningExtension
import kotlinx.serialization.Serializable

@Serializable
internal data class SigningSettings(
    override val required: Boolean? = null,
    override val useGpgCmd: Boolean? = null,
    override val useInMemoryPgpKeys: InMemoryPgpKeys? = null,
    override val sign: Set<@Serializable(with = SignContentPolymorphicSerializer::class) Any>? = null,
    override val enabled: Boolean = true,
    val generateGpg: GenerateGgp? = null,
) : SigningExtension(), EnabledSettings
