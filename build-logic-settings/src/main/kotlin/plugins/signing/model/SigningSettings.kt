package plugins.signing.model

import gradle.plugins.signing.InMemoryPgpKeys
import gradle.plugins.signing.SignFile
import gradle.plugins.signing.SigningExtension
import gradle.project.EnabledSettings
import kotlinx.serialization.Serializable

@Serializable
internal data class SigningSettings(
    override val required: Boolean? = null,
    override val useGpgCmd: Boolean? = null,
    override val useInMemoryPgpKeys: InMemoryPgpKeys? = null,
    override val signTasks: List<String>? = null,
    override val signConfigurations: List<String>? = null,
    override val signPublications: List<String>? = null,
    override val signArtifacts: List<String>? = null,
    override val signFiles: List<String>? = null,
    override val signClassifierFiles: List<SignFile>? = null,
    override val enabled: Boolean = true,
    val generateGpg: GenerateGgp? = null,
) : SigningExtension(), EnabledSettings
