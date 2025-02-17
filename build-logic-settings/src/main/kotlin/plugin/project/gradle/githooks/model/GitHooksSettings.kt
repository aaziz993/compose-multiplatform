package plugin.project.gradle.githooks.model

import kotlinx.serialization.Serializable

@Serializable
internal data class GitHooksSettings(
    override val hooks: Map<String, String>? = null,
    val hooksFiles: Map<String, String>? = null,
    val hooksUrls: Map<String, String>? = null,
    override val repoRoot: String? = null,
    val enabled: Boolean = true,
) : GitHooksExtension
