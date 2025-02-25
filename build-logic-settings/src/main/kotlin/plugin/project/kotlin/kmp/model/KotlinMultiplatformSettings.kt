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
import plugin.project.kotlin.model.language.KotlinTarget

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
    val targets: List<KotlinTarget>?=null,
    val hierarchy: LinkedHashMap<String, List<String>>? = null,
    val sourceSets: List<KotlinSourceSet>? = null,
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
