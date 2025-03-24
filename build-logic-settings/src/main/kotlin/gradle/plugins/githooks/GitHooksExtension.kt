package gradle.plugins.githooks

import gradle.accessors.gitHooks
import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.api.trySet
import java.net.URI
import org.gradle.api.initialization.Settings

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

    val hooksFiles: Map<String, String>?

    val hooksUrls: Map<String, String>?

    context(settings: Settings)
    fun applyTo() =
        settings.pluginManager.withPlugin(settings.libs.plugins.plugin("gradlePreCommitGitHooks").id) {
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
