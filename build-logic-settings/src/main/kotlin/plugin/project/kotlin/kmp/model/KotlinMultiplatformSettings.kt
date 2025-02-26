package plugin.project.kotlin.kmp.model

import gradle.id
import gradle.kotlin
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import plugin.project.java.model.Jar
import plugin.project.java.model.JavaToolchainSpec
import plugin.project.java.model.application.JavaApplication
import plugin.project.kotlin.cocoapods.model.CocoapodsSettings
import plugin.project.kotlin.model.language.KotlinSourceSet
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
    val targets: List<KotlinTarget>? = null,
    val hierarchy: LinkedHashMap<String, List<String>>? = null,
    val sourceSets: LinkedHashMap<String, KotlinSourceSet>? = null,
    val application: JavaApplication? = null,
    val jar: Jar? = null,
    val cocoapods: CocoapodsSettings = CocoapodsSettings(),
) : KotlinMultiplatformExtension {

    context(Project)
    fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("kotlin.multiplatform").id) {
            super.applyTo(kotlin)

            projectProperties.kotlin.targets?.forEach { target -> target.applyTo() }

            kotlin.applyDefaultHierarchyTemplate {
                common {
                    hierarchy?.forEach { (name, group) ->
                        group(name) {
                            group.forEach { targetName ->
                                when (targetName) {
                                    "jvm" -> withJvm()
                                    "androidTarget" -> withAndroidTarget()
                                    "native" -> group(targetName) {
                                        withNative()
                                    }

                                    "androidNative" -> group(targetName) {
                                        withAndroidNative()
                                    }

                                    "androidNativeArm32" -> withAndroidNativeArm32()
                                    "androidNativeArm64" -> withAndroidNativeArm64()
                                    "androidNativeX86" -> withAndroidNativeX86()
                                    "androidNativeX64" -> withAndroidNativeX64()
                                    "apple" -> group(targetName) {
                                        withApple()
                                    }

                                    "ios" -> group(targetName) {
                                        withIos()
                                    }

                                    "iosArm64" -> withIosArm64()
                                    "iosX64" -> withIosX64()
                                    "iosSimulatorArm64" -> withIosSimulatorArm64()
                                    "tvos" -> group(targetName) {
                                        withTvos()
                                    }

                                    "tvosArm64" -> withTvosArm64()
                                    "tvosX64" -> withTvosX64()
                                    "tvosSimulatorArm64" -> withTvosSimulatorArm64()
                                    "macos" -> group(targetName) {
                                        withMacos()
                                    }

                                    "macosArm64" -> withMacosArm64()
                                    "macosX64" -> withMacosX64()
                                    "watchos" -> group(targetName) {
                                        withWatchos()
                                    }

                                    "watchosArm32" -> withWatchosArm32()
                                    "watchosArm64" -> withWatchosArm64()
                                    "watchosDeviceArm64" -> withWatchosDeviceArm64()
                                    "watchosSimulatorArm64" -> withWatchosSimulatorArm64()
                                    "linux" -> group(targetName) {
                                        withLinux()
                                    }

                                    "linuxArm64" -> withLinuxArm64()
                                    "linuxX64" -> withLinuxX64()
                                    "mingw" -> group(targetName) {
                                        withMingw()
                                    }

                                    "mingwX64" -> withMingwX64()
                                    "js" -> withJs()
                                    "wasmJs" -> withWasmJs()
                                    "wasmWasi" -> withWasmWasi()
                                    else -> group(targetName)
                                }
                            }
                        }
                    }
                }
            }
        }
}
