package plugin.project.kotlin.model

import gradle.trySet
import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.plugin.LanguageSettingsBuilder
import plugin.project.apple.model.CocoapodsSettings
import plugin.project.kotlin.model.language.LanguageSettings
import plugin.project.kotlin.model.language.SourceSet
import plugin.project.kotlin.model.language.android.KotlinAndroidTarget
import plugin.project.kotlin.model.language.jvm.KotlinJvmTarget
import plugin.project.kotlin.model.language.nat.KotlinNativeTarget
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
    val androidNativeArm32: LinkedHashMap<String, KotlinNativeTarget>? = null,
    val androidNativeArm64: LinkedHashMap<String, KotlinNativeTarget>? = null,
    val androidNativeX86: LinkedHashMap<String, KotlinNativeTarget>? = null,
    val androidNativeX64: LinkedHashMap<String, KotlinNativeTarget>? = null,
    val iosArm64: LinkedHashMap<String, KotlinNativeTarget>? = null,
    val iosX64: LinkedHashMap<String, KotlinNativeTarget>? = null,
    val iosSimulatorArm64: LinkedHashMap<String, KotlinNativeTarget>? = null,
    val watchosArm32: LinkedHashMap<String, KotlinNativeTarget>? = null,
    val watchosArm64: LinkedHashMap<String, KotlinNativeTarget>? = null,
    val watchosX64: LinkedHashMap<String, KotlinNativeTarget>? = null,
    val watchosSimulatorArm64: LinkedHashMap<String, KotlinNativeTarget>? = null,
    val tvosArm64: LinkedHashMap<String, KotlinNativeTarget>? = null,
    val tvosX64: LinkedHashMap<String, KotlinNativeTarget>? = null,
    val tvosSimulatorArm64: LinkedHashMap<String, KotlinNativeTarget>? = null,
    val macosArm64: LinkedHashMap<String, KotlinNativeTarget>? = null,
    val macosX64: LinkedHashMap<String, KotlinNativeTarget>? = null,
    val linuxArm64: LinkedHashMap<String, KotlinNativeTarget>? = null,
    val linuxX64: LinkedHashMap<String, KotlinNativeTarget>? = null,
    val mingwX64: LinkedHashMap<String, KotlinNativeTarget>? = null,
    val js: LinkedHashMap<String, KotlinJsTarget>? = null,
    val wasmJs: LinkedHashMap<String, KotlinWasmJsTarget>? = null,
    val targetGroups: LinkedHashMap<String, List<String>>? = null,
    val sourceSets: LinkedHashMap<String, SourceSet>? = null,
    val cocoapods: CocoapodsSettings = CocoapodsSettings(),
) : LanguageSettings {

    val hasAndroidNativeTargets by lazy {
        (androidNativeArm32 ?: androidNativeArm64 ?: androidNativeX86 ?: androidNativeX64) != null
    }

    val hasIosTargets by lazy {
        (iosArm64 ?: iosX64 ?: iosSimulatorArm64) != null
    }

    val hasWatchosTargets by lazy {
        (watchosArm32 ?: watchosArm64 ?: watchosX64 ?: watchosSimulatorArm64) != null
    }

    val hasTvosTargets by lazy {
        (tvosArm64 ?: tvosX64 ?: tvosSimulatorArm64) != null
    }

    val hasMacosTargets by lazy {
        (macosArm64 ?: macosX64) != null
    }

    val hasAppleTargets by lazy {
        hasIosTargets || hasWatchosTargets || hasTvosTargets || hasMacosTargets
    }

    val hasLinuxTargets by lazy {
        (linuxArm64 ?: linuxX64) != null
    }

    val hasNativeTargets by lazy {
        hasAndroidNativeTargets || hasAppleTargets || hasLinuxTargets
    }

    val hasTargets by lazy {
        (jvm ?: android ?: js ?: wasmJs) != null || hasNativeTargets
    }

    fun applyTo(builder: LanguageSettingsBuilder) {
        builder::languageVersion trySet languageVersion
        builder::apiVersion trySet apiVersion
        builder::progressiveMode trySet progressiveMode
        languageFeatures?.forEach(builder::enableLanguageFeature)
        optIns?.forEach(builder::optIn)
    }
}
