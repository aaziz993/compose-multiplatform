package plugin.project.kotlin.ksp.model

import kotlinx.serialization.Serializable

@Serializable
internal data class KspSettings(
    override val useKsp2: Boolean? = null,
    override val commandLineArgumentProviders: List<String>? = null,
    override val excludedProcessors: List<String>? = null,
    override val excludedSources: List<String>? = null,
    override val arguments: Map<String, String>? = null,
    override val allWarningsAsErrors: Boolean? = null,
    val processors: List<String>? = null,
    val enabled: Boolean = true,
) : KspExtension
