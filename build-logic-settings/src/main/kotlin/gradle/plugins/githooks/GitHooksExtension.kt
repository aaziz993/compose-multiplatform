package gradle.plugins.githooks

import gradle.accessors.gitHooks
import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.api.trySet
import java.io.File
import java.net.URI
import kotlin.collections.component1
import kotlin.collections.component2
import org.danilopianini.gradle.git.hooks.CommitMsgScriptContext
import org.danilopianini.gradle.git.hooks.CommonScriptContext
import org.danilopianini.gradle.git.hooks.GitHooksExtension
import org.danilopianini.gradle.git.hooks.ScriptContext
import org.gradle.api.initialization.Settings
import org.gradle.api.logging.Logging

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

    context(Settings)
    fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("gradlePreCommitGitHooks").id) {
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

        hooksUrls?.forEach { name, url ->
            gitHooks.hook(name) {
                from(URI(url).toURL())
            }
        }

        gitHooks::repoRoot trySet repoRoot?.let(rootDir::resolve)
    }
}
