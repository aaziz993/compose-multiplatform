package plugin.project.web.model

import kotlinx.serialization.Serializable

@Serializable
internal data class KotlinWebpackRule(
    val enabled: Boolean? = null,
    /**
     * Raw rule `test` field value. Needs to be wrapped in quotes when using string notation.
     */
    val test: String? = null,
    val include: List<String>? = null,
    val exclude: List<String>? = null,
    /**
     * Validates the rule state just before it getting applied.
     * Returning false will skip the rule silently. To terminate the build instead, throw an error.
     */
    val validate: Boolean? = null,
)
