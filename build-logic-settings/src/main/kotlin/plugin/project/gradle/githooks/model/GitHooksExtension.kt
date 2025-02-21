package plugin.project.gradle.githooks.model

import gradle.trySet
import java.net.URI
import org.danilopianini.gradle.git.hooks.GitHooksExtension
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
    fun applyTo(extension: GitHooksExtension) {
        hooks?.forEach { name, script ->
            extension.hook(name) {
                from { script }
            }
        }

        hooksFiles?.forEach { name, file ->
            extension.hook(name) {
                from(rootDir.resolve(file))
            }
        }

        hooksUrls?.forEach { name, file ->
            extension.hook(name) {
                from(URI(file).toURL())
            }
        }

        extension::repoRoot trySet repoRoot?.let(rootDir::resolve)
    }
}
