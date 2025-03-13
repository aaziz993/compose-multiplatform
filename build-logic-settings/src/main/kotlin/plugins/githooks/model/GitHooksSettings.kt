package plugins.githooks.model

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.plugins.githooks.GitHooksExtension
import gradle.project.EnabledSettings
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
