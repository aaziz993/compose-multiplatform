package plugin.project.kotlin.model

import gradle.projectProperties
import gradle.trySet
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.jetbrains.kotlin.gradle.plugin.LanguageSettingsBuilder
import plugin.project.apple.model.CocoapodsSettings
import plugin.project.kotlin.model.language.LanguageSettings
import plugin.project.kotlin.model.language.SourceSet
import plugin.project.kotlin.model.language.android.KotlinAndroidTarget
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
    val jvm: LinkedHashMap<String, KotlinJvmTarget>? = null,
    val android: LinkedHashMap<String, KotlinAndroidTarget>? = null,
    val ios: LinkedHashMap<String, KotlinAndroidTarget>? = null,
    val js: LinkedHashMap<String, KotlinJsTarget>? = null,
    val wasmJs: LinkedHashMap<String, KotlinWasmJsTarget>? = null,
    val targetGroups: LinkedHashMap<String, List<String>>? = null,
    val sourceSets: LinkedHashMap<String, SourceSet>? = null,
    val cocoapods: CocoapodsSettings = CocoapodsSettings(),
) : LanguageSettings {

    val hasTargets by lazy {
        (jvm ?: android ?: js ?: wasmJs) == null
    }

    fun applyTo(builder: LanguageSettingsBuilder) {
        builder::languageVersion trySet languageVersion
        builder::apiVersion trySet apiVersion
        builder::progressiveMode trySet progressiveMode
        languageFeatures?.forEach(builder::enableLanguageFeature)
        optIns?.forEach(builder::optIn)
    }
}
