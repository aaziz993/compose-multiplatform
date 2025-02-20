package plugin.project.gradle.githooks

import gradle.projectProperties
import gradle.trySet
import java.net.URI
import org.danilopianini.gradle.git.hooks.GradleGitHooksPlugin
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.gitHooks
import org.gradle.kotlin.dsl.withType

internal fun Settings.configureGitHooksExtension() =
    plugins.withType<GradleGitHooksPlugin> {
        projectProperties.settings.gradle.gitHooks.let { gitHooks ->
            gitHooks {
                gitHooks.hooks?.forEach { name, script ->
                    hook(name) {
                        from { script }
                    }
                }

                gitHooks.hooksFiles?.forEach { name, file ->
                    hook(name) {
                        from(rootDir.resolve(file))
                    }
                }

                gitHooks.hooksUrls?.forEach { name, file ->
                    hook(name) {
                        from(URI(file).toURL())
                    }
                }

                ::repoRoot trySet gitHooks.repoRoot?.let(rootDir::resolve)
            }
        }
    }
