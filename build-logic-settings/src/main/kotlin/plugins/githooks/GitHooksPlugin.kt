package plugins.githooks

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import plugins.gradle.githooks.model.GitHooksSettings

internal class GitHooksPlugin : Plugin<Settings> {

    override fun apply(target: Settings) {
        with(target) {
            projectProperties.plugins.gitHooks.takeIf(GitHooksSettings::enabled)?.let { gitHooks ->
                plugins.apply(settings.libs.plugins.plugin("gradle-pre-commit-git-hooks").id)

                gitHooks.applyTo()
            }
        }
    }
}
