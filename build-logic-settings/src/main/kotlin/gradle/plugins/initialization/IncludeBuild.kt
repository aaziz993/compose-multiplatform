package gradle.plugins.initialization

import gradle.api.initialization.ConfigurableIncludedBuild
import kotlinx.serialization.Serializable

@Serializable
internal data class IncludeBuild(
    val rootProject: String,
    val configuration: ConfigurableIncludedBuild? = null,
)
