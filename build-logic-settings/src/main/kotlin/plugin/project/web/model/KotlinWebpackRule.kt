package plugin.project.web.model

internal interface KotlinWebpackRule {

    val enabled: Boolean?

    /**
     * Raw rule `test` field value. Needs to be wrapped in quotes when using string notation.
     */
    val test: String?
    val include: List<String>?
    val exclude: List<String>?

    /**
     * Validates the rule state just before it getting applied.
     * Returning false will skip the rule silently. To terminate the build instead, throw an error.
     */
    val validate: Boolean?
}
