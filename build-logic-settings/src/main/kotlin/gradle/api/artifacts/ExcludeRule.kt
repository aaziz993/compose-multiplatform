package gradle.api.artifacts

import kotlinx.serialization.Serializable

/**
 * An `ExcludeRule` is used to describe transitive dependencies that should be excluded when resolving
 * dependencies.
 */
@Serializable
internal data class ExcludeRule(
    /**
     * The exact name of the organization or group that should be excluded.
     */
    val group: String? = null,
    /**
     * The exact name of the module that should be excluded.
     */
    val module: String? = null,
)
