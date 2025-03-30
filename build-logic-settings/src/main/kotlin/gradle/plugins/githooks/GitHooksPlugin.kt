package gradle.plugins.githooks

import gradle.accessors.catalog.libs

import gradle.accessors.projectProperties
import gradle.plugins.githooks.model.GitHooksSettings
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings

internal class GitHooksPlugin : Plugin<Settings> {

    override fun apply(target: Settings) {
        with(target) {
            projectProperties.gitHooks?.takeIf{ pluginManager.hasPlugin("gitHooks") }?.let { gitHooks ->
                plugins.apply(libs.plugin("gradlePreCommitGitHooks").id)

                gitHooks.applyTo()
            }
        }
    }
}
