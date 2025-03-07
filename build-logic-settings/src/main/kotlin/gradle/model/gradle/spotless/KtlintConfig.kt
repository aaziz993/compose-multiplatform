package gradle.model.gradle.spotless

import kotlinx.serialization.Serializable

@Serializable
internal data class KtlintConfig(
    val version: String? = null,
    val editorConfigPath: String? = null,
    val editorConfigOverride: Map<String, String>? = null,
    val customRuleSets: List<String>? = null,
)
