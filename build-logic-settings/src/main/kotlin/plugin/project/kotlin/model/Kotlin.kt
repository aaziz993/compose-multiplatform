package plugin.project.kotlin.model

import gradle.trySet
import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.plugin.LanguageSettingsBuilder
import plugin.project.cocoapods.model.CocoapodsSettings
import plugin.project.kotlin.model.language.LanguageSettings
import plugin.project.kotlin.model.language.jvm.KotlinJvmTarget
import plugin.project.kotlin.model.language.web.KotlinJsTarget
import plugin.project.kotlin.model.language.web.KotlinWasmJsTarget

@Serializable
internal data class Kotlin(
    override val languageVersion: String? = null,
    override val apiVersion: String? = null,
    override val progressiveMode: Boolean? = null,
    override val languageFeatures: Set<String>? = null,
    override val optIns: Set<String>? = null,
    val jvm: List<KotlinJvmTarget>? = null,
    val js: List<KotlinJsTarget>? = null,
    val wasmJs: List<KotlinWasmJsTarget>? = null,
    val targetGroups: LinkedHashMap<String, List<String>>? = null,
    val sourceSets: LinkedHashMap<String, SourceSet>? = null,
    val cocoapods: CocoapodsSettings = CocoapodsSettings(),
) : LanguageSettings {

    fun applyTo(builder: LanguageSettingsBuilder) {
        builder::languageVersion trySet languageVersion
        builder::apiVersion trySet apiVersion
        builder::progressiveMode trySet progressiveMode
        languageFeatures?.forEach(builder::enableLanguageFeature)
        optIns?.forEach(builder::optIn)
    }
}
