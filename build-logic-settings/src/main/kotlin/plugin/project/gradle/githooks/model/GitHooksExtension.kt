package plugin.project.gradle.githooks.model

import gradle.gitHooks
import gradle.trySet
import java.net.URI
import org.gradle.api.initialization.Settings

/**
 * DSL entry point, to be applied to [settings].gradle.kts.
 */
internal interface GitHooksExtension {

    val hooks: Map<String, String>?

    val hooksFiles: Map<String, String>?

    val hooksUrls: Map<String, String>?

    /**
     * The git repository root. If unset, it will be searched recursively from the project root towards the
     * filesystem root.
     */
    val repoRoot: String?

    context(Settings)
    fun applyTo() {
        hooks?.forEach { name, script ->
            gitHooks.hook(name) {
                from { script }
            }
        }

        hooksFiles?.forEach { name, file ->
            gitHooks.hook(name) {
                from(rootDir.resolve(file))
            }
        }

        hooksUrls?.forEach { name, file ->
            gitHooks.hook(name) {
                from(URI(file).toURL())
            }
        }

        gitHooks::repoRoot trySet repoRoot?.let(rootDir::resolve)
    }
}
