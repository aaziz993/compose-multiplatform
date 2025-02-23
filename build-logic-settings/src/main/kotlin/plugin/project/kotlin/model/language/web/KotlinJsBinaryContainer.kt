package plugin.project.kotlin.model.language.web

import kotlinx.serialization.Serializable

@Serializable
internal data class KotlinJsBinaryContainer(
    val executable: Boolean? = null,
    val library: Boolean? = null,
)
