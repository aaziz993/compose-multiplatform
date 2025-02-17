package plugin.project.gradle.githooks

import gradle.amperProjectExtraProperties
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import org.gradle.api.initialization.Settings

internal class GitHooksluginPart(private val settings: Settings) {

    val needToApply: Boolean by lazy {
        settings.amperProjectExtraProperties.settings.gradle.gitHooks.enabled
    }

    init {
        settings.plugins.apply(settings.libs.plugins.plugin("gradle-pre-commit-git-hooks").id)

        applySettings()
    }

    fun applySettings() = with(settings) {
        configureGitHooksExtension()
    }
}
