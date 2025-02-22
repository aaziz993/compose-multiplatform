package plugin.project.gradle.githooks

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import plugin.project.gradle.githooks.model.GitHooksSettings

internal class GitHooksluginPart : Plugin<Settings> {

    override fun apply(target: Settings) {
        with(target) {
           settings.projectProperties.plugins.gitHooks.takeIf(GitHooksSettings::enabled)?.let { gitHooks ->
                settings.plugins.apply(settings.libs.plugins.plugin("gradle-pre-commit-git-hooks").id)

                applySettings()
            }
        }
    }

    private fun Settings.applySettings() {
        configureGitHooksExtension()
    }
}
