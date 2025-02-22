package plugin.project.kotlin.model

import gradle.trySet
import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.plugin.LanguageSettingsBuilder
import plugin.project.kotlin.model.language.LanguageSettings
import plugin.project.kotlin.model.target.Target

@Serializable
internal data class Kotlin(
    override val languageVersion: String? = null,
    override val apiVersion: String? = null,
    override val progressiveMode: Boolean? = null,
    override val languageFeatures: Set<String>? = null,
    override val optIns: Set<String>? = null,
    val targets: List<Target> = emptyList(),
    val targetGroups: List<TargetGroup>? = null,
    val sourceSets: List<SourceSet>? = null
) : LanguageSettings {

    fun applyTo(builder: LanguageSettingsBuilder) {
        builder::languageVersion trySet languageVersion
        builder::apiVersion trySet apiVersion
        builder::progressiveMode trySet progressiveMode
        languageFeatures?.forEach(builder::enableLanguageFeature)
        optIns?.forEach(builder::optIn)
    }
}
