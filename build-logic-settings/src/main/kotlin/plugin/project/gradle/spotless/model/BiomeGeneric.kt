package plugin.project.gradle.spotless.model

import kotlinx.serialization.Serializable

@Serializable
internal data class BiomeGeneric(
    override val version: String?,
    val language: String? = null,
) : RomeStepConfig
