package gradle.plugins.initialization.includebuild

import gradle.api.initialization.ConfigurableIncludedBuild
import kotlinx.serialization.Serializable

@Serializable
internal data class IncludeBuild(
    val rootProject: String,
    val configuration: ConfigurableIncludedBuild? = null,
)
