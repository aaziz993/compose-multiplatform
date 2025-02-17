package plugin.project.gradle.githooks.model

/**
 * DSL entry point, to be applied to [settings].gradle.kts.
 */
internal interface GitHooksExtension {

    val hooks: Map<String, String>?

    /**
     * The git repository root. If unset, it will be searched recursively from the project root towards the
     * filesystem root.
     */
    val repoRoot: String?
}
