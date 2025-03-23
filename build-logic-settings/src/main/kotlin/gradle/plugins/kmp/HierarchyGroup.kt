package gradle.plugins.kmp

import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.plugin.KotlinHierarchyBuilder

@Serializable
internal data class HierarchyGroup(
    val group: String,
    val aliases: Set<String>
) {

    fun applyTo(receiver: KotlinHierarchyBuilder) {
        builder.group(group) {
            aliases.forEach { alias ->
                when (alias) {
                    "jvm" -> withJvm()
                    "android" -> withAndroidTarget()
                    "native" -> group(alias) {
                        withNative()
                    }

                    "androidNative" -> group(alias) {
                        withAndroidNative()
                    }

                    "androidNativeArm32" -> withAndroidNativeArm32()
                    "androidNativeArm64" -> withAndroidNativeArm64()
                    "androidNativeX86" -> withAndroidNativeX86()
                    "androidNativeX64" -> withAndroidNativeX64()
                    "apple" -> group(alias) {
                        withApple()
                    }

                    "ios" -> group(alias) {
                        withIos()
                    }

                    "iosArm64" -> withIosArm64()
                    "iosX64" -> withIosX64()
                    "iosSimulatorArm64" -> withIosSimulatorArm64()
                    "tvos" -> group(alias) {
                        withTvos()
                    }

                    "tvosArm64" -> withTvosArm64()
                    "tvosX64" -> withTvosX64()
                    "tvosSimulatorArm64" -> withTvosSimulatorArm64()
                    "macos" -> group(alias) {
                        withMacos()
                    }

                    "macosArm64" -> withMacosArm64()
                    "macosX64" -> withMacosX64()
                    "watchos" -> group(alias) {
                        withWatchos()
                    }

                    "watchosArm32" -> withWatchosArm32()
                    "watchosArm64" -> withWatchosArm64()
                    "watchosDeviceArm64" -> withWatchosDeviceArm64()
                    "watchosSimulatorArm64" -> withWatchosSimulatorArm64()
                    "linux" -> group(alias) {
                        withLinux()
                    }

                    "linuxArm64" -> withLinuxArm64()
                    "linuxX64" -> withLinuxX64()
                    "mingw" -> group(alias) {
                        withMingw()
                    }

                    "mingwX64" -> withMingwX64()
                    "js" -> withJs()
                    "wasmJs" -> withWasmJs()
                    "wasmWasi" -> withWasmWasi()
                    else -> group(alias)
                }
            }
        }
    }
}

internal object HierarchyAliasTransformingSerializer : KeyTransformingSerializer<HierarchyGroup>(
    HierarchyGroup.serializer(),
    "group",
    "aliases",
)
