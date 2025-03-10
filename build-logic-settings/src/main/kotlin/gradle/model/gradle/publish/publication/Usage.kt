package gradle.model.gradle.publish.publication

import kotlinx.serialization.Serializable

@Serializable
internal data class Usage(
    val usage: String,
    val strategy: VariantVersionMappingStrategy
)
