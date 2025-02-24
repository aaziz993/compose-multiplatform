package plugin.project.kotlin.kmp.model

import gradle.id
import gradle.kotlin
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import plugin.project.java.model.Jar
import plugin.project.java.model.JavaToolchainSpec
import plugin.project.java.model.application.JavaApplication
import plugin.project.kotlin.cocoapods.model.CocoapodsSettings
import plugin.project.kotlin.model.KotlinSourceSet
import plugin.project.kotlin.model.language.KotlinCommonCompilerOptions
import plugin.project.kotlin.model.language.KotlinMultiplatformExtension
import plugin.project.kotlin.model.language.android.KotlinAndroidTargetSettings
import plugin.project.kotlin.model.language.jvm.KotlinJvmTargetSettings
import plugin.project.kotlin.model.language.nat.KotlinNativeTargetSettings
import plugin.project.kotlin.model.language.web.KotlinJsTargetSettings
import plugin.project.kotlin.model.language.web.KotlinWasmJsTargetSettings

@Serializable
internal data class KotlinMultiplatformSettings(
    override val withSourcesJar: Boolean? = null,
    override val jvmToolchainSpec: JavaToolchainSpec? = null,
    override val jvmToolchain: Int? = null,
    override val kotlinDaemonJvmArgs: List<String>? = null,
    override val compilerVersion: String? = null,
    override val coreLibrariesVersion: String? = null,
    override val explicitApi: ExplicitApiMode? = null,
    override val compilerOptions: KotlinCommonCompilerOptions? = null,
    val jvm: LinkedHashMap<String, KotlinJvmTargetSettings>? = null,
    val android: LinkedHashMap<String, KotlinAndroidTargetSettings>? = null,
    val androidNativeArm32: LinkedHashMap<String, KotlinNativeTargetSettings>? = null,
    val androidNativeArm64: LinkedHashMap<String, KotlinNativeTargetSettings>? = null,
    val androidNativeX86: LinkedHashMap<String, KotlinNativeTargetSettings>? = null,
    val androidNativeX64: LinkedHashMap<String, KotlinNativeTargetSettings>? = null,
    val iosArm64: LinkedHashMap<String, KotlinNativeTargetSettings>? = null,
    val iosX64: LinkedHashMap<String, KotlinNativeTargetSettings>? = null,
    val iosSimulatorArm64: LinkedHashMap<String, KotlinNativeTargetSettings>? = null,
    val watchosArm32: LinkedHashMap<String, KotlinNativeTargetSettings>? = null,
    val watchosArm64: LinkedHashMap<String, KotlinNativeTargetSettings>? = null,
    val watchosX64: LinkedHashMap<String, KotlinNativeTargetSettings>? = null,
    val watchosSimulatorArm64: LinkedHashMap<String, KotlinNativeTargetSettings>? = null,
    val tvosArm64: LinkedHashMap<String, KotlinNativeTargetSettings>? = null,
    val tvosX64: LinkedHashMap<String, KotlinNativeTargetSettings>? = null,
    val tvosSimulatorArm64: LinkedHashMap<String, KotlinNativeTargetSettings>? = null,
    val macosArm64: LinkedHashMap<String, KotlinNativeTargetSettings>? = null,
    val macosX64: LinkedHashMap<String, KotlinNativeTargetSettings>? = null,
    val linuxArm64: LinkedHashMap<String, KotlinNativeTargetSettings>? = null,
    val linuxX64: LinkedHashMap<String, KotlinNativeTargetSettings>? = null,
    val mingwX64: LinkedHashMap<String, KotlinNativeTargetSettings>? = null,
    val js: LinkedHashMap<String, KotlinJsTargetSettings>? = null,
    val wasmJs: LinkedHashMap<String, KotlinWasmJsTargetSettings>? = null,
    val hierarchy: LinkedHashMap<String, List<String>>? = null,
    val sourceSets: LinkedHashMap<String, KotlinSourceSet>? = null,
    val application: JavaApplication? = null,
    val jar: Jar? = null,
    val cocoapods: CocoapodsSettings = CocoapodsSettings(),
) : KotlinMultiplatformExtension {

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

    context(Project)
    fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("kotlin.multiplatform").id) {
            super.applyTo(kotlin)
            kotlin.applyDefaultHierarchyTemplate {
                common {
                    hierarchy?.forEach { (name, group) ->
                        group(name) {
                            group.forEach { targetName ->
                                when (targetName) {
                                    "jvm" -> withJvm()
                                    "android" -> withAndroidTarget()
                                    "ios" -> group("ios") {
                                        withIos()
                                    }

                                    "iosArm64" -> withIosArm64()
                                    "iosX64" -> withIosX64()
                                    "iosSimulatorArm64" -> withIosSimulatorArm64()
                                    "js" -> withJs()
                                    "wasm" -> withWasmJs()
                                    else -> group(targetName)
                                }
                            }
                        }
                    }
                }
            }
        }
}
