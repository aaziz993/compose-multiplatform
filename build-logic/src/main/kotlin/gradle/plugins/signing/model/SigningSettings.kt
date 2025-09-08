package gradle.plugins.signing.model

import kotlinx.serialization.Serializable

@Serializable
public data class SigningSettings(
    val generateGgp: GenerateGgp
)
