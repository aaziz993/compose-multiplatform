package gradle.plugins.kotlin.apollo

import kotlinx.serialization.Serializable

@Serializable
internal data class KotlinSourceSetService(
    val sourceFolder: String,
    val nameSuffix: String,
    val service: Service? = null
)
