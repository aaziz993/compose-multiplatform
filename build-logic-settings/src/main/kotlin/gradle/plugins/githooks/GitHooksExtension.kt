package gradle.plugins.githooks

import gradle.accessors.gitHooks
import gradle.accessors.settings
import gradle.reflect.trySet
import java.net.URI
import kotlinx.serialization.Serializable
import org.gradle.api.initialization.Settings

/**
 * DSL entry point, to be applied to [settings].gradle.kts.
 */
@Serializable
internal data class GitHooksExtension(
    val hooks: Map<String, String>? = null,
    /**
     * The git repository root. If unset, it will be searched recursively from the project root towards the
     * filesystem root.
     */
    val repoRoot: String? = null,
    val hooksFiles: Map<String, String>? = null,
    val hooksUrls: Map<String, String>? = null,
) {

    context(Settings)
    fun applyTo() =
        settings.pluginManager.withPlugin("org.danilopianini.gradle-pre-commit-git-hooks") {
            hooks?.forEach { name, script ->
                settings.gitHooks.hook(name) {
                    from { script }
                }
            }

            hooksFiles?.forEach { name, file ->
                settings.gitHooks.hook(name) {
                    from(settings.settingsDir.resolve(file))
                }
            }

            hooksUrls?.forEach { name, url ->
                settings.gitHooks.hook(name) {
                    from(URI(url).toURL())
                }
            }

            settings.gitHooks::repoRoot trySet repoRoot?.let(settings.settingsDir::resolve)
        }
}
