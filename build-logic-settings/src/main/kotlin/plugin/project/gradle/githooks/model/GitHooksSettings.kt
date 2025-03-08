package plugin.project.gradle.githooks.model

import gradle.id
import gradle.libs
import gradle.model.gradle.githooks.GitHooksExtension
import gradle.model.project.EnabledSettings
import gradle.plugin
import gradle.plugins
import kotlinx.serialization.Serializable
import org.gradle.api.initialization.Settings

@Serializable
internal data class GitHooksSettings(
    override val hooks: Map<String, String>? = null,
    override val hooksFiles: Map<String, String>? = null,
    override val hooksUrls: Map<String, String>? = null,
    override val repoRoot: String? = null,
    override val enabled: Boolean = true,
) : GitHooksExtension, EnabledSettings {

    context(Settings)
    override fun applyTo() =
        pluginManager.withPlugin(libs.plugins.plugin("gradle-pre-commit-git-hooks").id) {
            super.applyTo()
        }
}
