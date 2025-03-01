package plugin.project.kotlin.kmp.model

import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.plugin.KotlinHierarchyBuilder

@Serializable
internal data class HierarchyAlias(
    val alias: String,
    val aliases: List<String>
){
    fun applyTo(builder: KotlinHierarchyBuilder){
        builder.group(alias) {
            aliases.forEach { targetName ->
                when (targetName) {
                    "jvm" -> withJvm()
                    "android" -> withAndroidTarget()
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

internal object HierarchyAliasTransformingSerializer : KeyTransformingSerializer<HierarchyAlias>(
    HierarchyAlias.serializer(),
    "alias",
)
