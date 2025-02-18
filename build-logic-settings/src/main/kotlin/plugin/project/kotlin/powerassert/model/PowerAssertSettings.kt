package plugin.project.kotlin.powerassert.model

import kotlinx.serialization.Serializable

@Serializable
internal data class PowerAssertSettings(
    override val functions: Set<String>? = null,
    override val includedSourceSets: Set<String>? = null,
    val enabled: Boolean = true
) : PowerAssertGradleExtension
