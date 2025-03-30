package gradle.plugins.githooks.model

import gradle.accessors.catalog.libs

import gradle.plugins.githooks.GitHooksExtension
import gradle.api.EnabledSettings
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
        settings.pluginManager.withPlugin(settings.libs.plugin("gradle-pre-commit-git-hooks").id) {
            super.applyTo()
        }
}
