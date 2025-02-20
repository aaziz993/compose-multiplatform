package plugin.project.gradle.githooks

import gradle.projectProperties
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.gitHooks
import plugin.project.gradle.githooks.model.GitHooksSettings

internal class GitHooksluginPart : Plugin<Settings> {

    override fun apply(target: Settings) {
        with(target) {
            projectProperties.settings.gradle.gitHooks.takeIf(GitHooksSettings::enabled)?.let { gitHooks ->
                settings.plugins.apply(settings.libs.plugins.plugin("gradle-pre-commit-git-hooks").id)

                applySettings()
            }
        }
    }

    private fun Settings.applySettings() {
        configureGitHooksExtension()
    }
}
