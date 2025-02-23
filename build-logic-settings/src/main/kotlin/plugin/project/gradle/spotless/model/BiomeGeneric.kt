package plugin.project.gradle.spotless.model

import kotlinx.serialization.Serializable

@Serializable
internal data class BiomeGeneric(
    override val configPath: String? = null,
    override val downloadDir: String? = null,
    override val pathToExe: String? = null,
    override val version: String? = null,
) : RomeStepConfig
