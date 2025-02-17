package plugin.project.gradle.githooks

import gradle.amperProjectExtraProperties
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import org.gradle.api.initialization.Settings

internal class GitHooksluginPart(private val settings: Settings) {

    private val gitHooks by lazy {
        settings.amperProjectExtraProperties.settings.gradle.gitHooks
    }

    val needToApply: Boolean by lazy {
        gitHooks.enabled
    }

    fun applyBeforeEvaluate() {
        settings.plugins.apply(settings.libs.plugins.plugin("git-hooks").id)

        applySettings()
    }

    fun applySettings() = with(settings) {
        configureGitHooksExtension()
    }
}
