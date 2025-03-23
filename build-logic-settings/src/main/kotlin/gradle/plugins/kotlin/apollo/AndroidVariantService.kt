package gradle.plugins.kotlin.apollo

import kotlinx.serialization.Serializable

@Serializable
internal data class AndroidVariantService(
    val sourceFolder: String,
    val nameSuffix: String,
    val service: Service? = null
)
