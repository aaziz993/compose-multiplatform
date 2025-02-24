package plugin.project.model

import kotlinx.serialization.Serializable

@Serializable
internal data class Settings(
    val application: Boolean = false,
    val layout: Layout = Layout.DEFAULT
)
