package gradle.model.gradle.spotless

import com.diffplug.gradle.spotless.BaseKotlinExtension
import kotlinx.serialization.Serializable

@Serializable
internal data class KtlintConfig(
    val version: String? = null,
    val editorConfigPath: String? = null,
    val editorConfigOverride: Map<String, String>? = null,
    val customRuleSets: List<String>? = null,
) {

    fun applyTo(config: BaseKotlinExtension.KtlintConfig) {
        editorConfigPath?.let(config::setEditorConfigPath)
        editorConfigOverride?.let(config::editorConfigOverride)
        customRuleSets?.let(config::customRuleSets)
    }
}
