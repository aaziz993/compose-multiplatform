package gradle.api.initialization

import gradle.api.artifacts.dsl.RepositoryHandler
import kotlinx.serialization.Serializable

@Serializable
internal data class PluginManagement(
    val repositories: RepositoryHandler? = null,
)
