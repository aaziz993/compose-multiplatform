package plugins.githooks

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import plugins.githooks.model.GitHooksSettings

internal class GitHooksPlugin : Plugin<Settings> {

    override fun apply(target: Settings) {
        with(target) {
            projectProperties.plugins.gitHooks.takeIf(GitHooksSettings::enabled)?.let { gitHooks ->
                plugins.apply(libs.plugins.plugin("gradlePreCommitGitHooks").id)

                gitHooks.applyTo()
            }
        }
    }
}
