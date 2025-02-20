package plugin.project.model.target

import gradle.kotlin
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable(with = TargetSerializer::class)
internal data class Target(
    val type: TargetType,
    val targetName: String? = null
)

context(Project)
internal fun Target.add() = when (type) {
    TargetType.ANDROID -> targetName?.let(kotlin::androidTarget) ?: kotlin.androidTarget()
    TargetType.JVM -> targetName?.let(kotlin::jvm) ?: kotlin.jvm()
    TargetType.IOS_ARM64 -> targetName?.let(kotlin::iosArm64) ?: kotlin.iosArm64()
    TargetType.IOS_SIMULATOR_ARM64 -> targetName?.let(kotlin::iosSimulatorArm64) ?: kotlin.iosSimulatorArm64()
    TargetType.IOS_X64 -> targetName?.let(kotlin::iosX64) ?: kotlin.iosX64()
    TargetType.MACOS_X64 -> targetName?.let(kotlin::macosX64) ?: kotlin.macosX64()
    TargetType.MACOS_ARM64 -> targetName?.let(kotlin::macosArm64) ?: kotlin.macosArm64()
    TargetType.LINUX_X64 -> targetName?.let(kotlin::linuxX64) ?: kotlin.linuxX64()
    TargetType.LINUX_ARM64 -> targetName?.let(kotlin::linuxArm64) ?: kotlin.linuxArm64()
    TargetType.JS -> targetName?.let(kotlin::js) ?: kotlin.js()
    TargetType.WASM -> targetName?.let(kotlin::wasmJs) ?: kotlin.wasmJs()
    TargetType.TVOS_ARM64 -> targetName?.let(kotlin::tvosArm64) ?: kotlin.tvosArm64()
    TargetType.TVOS_X64 -> targetName?.let(kotlin::tvosX64) ?: kotlin.tvosX64()
    TargetType.TVOS_SIMULATOR_ARM64 -> targetName?.let(kotlin::tvosSimulatorArm64) ?: kotlin.tvosSimulatorArm64()
    TargetType.WATCHOS_ARM64 -> targetName?.let(kotlin::watchosArm64) ?: kotlin.watchosArm64()
    TargetType.WATCHOS_ARM32 -> targetName?.let(kotlin::watchosArm32) ?: kotlin.watchosArm32()
    TargetType.WATCHOS_DEVICE_ARM64 -> targetName?.let(kotlin::watchosDeviceArm64) ?: kotlin.watchosDeviceArm64()
    TargetType.WATCHOS_SIMULATOR_ARM64 -> targetName?.let(kotlin::watchosSimulatorArm64)
        ?: kotlin.watchosSimulatorArm64()

    TargetType.MINGW_X64 -> targetName?.let(kotlin::mingwX64) ?: kotlin.mingwX64()
    TargetType.ANDROID_NATIVE_ARM32 -> targetName?.let(kotlin::androidNativeArm32) ?: kotlin.androidNativeArm32()
    TargetType.ANDROID_NATIVE_ARM64 -> targetName?.let(kotlin::androidNativeArm64) ?: kotlin.androidNativeArm64()
    TargetType.ANDROID_NATIVE_X64 -> targetName?.let(kotlin::androidNativeX64) ?: kotlin.androidNativeX64()
    TargetType.ANDROID_NATIVE_X86 -> targetName?.let(kotlin::androidNativeX86) ?: kotlin.androidNativeX86()

    // These are not leaf platforms, thus - should not get here.
    TargetType.ANDROID_NATIVE, TargetType.MINGW, TargetType.WATCHOS,
    TargetType.IOS, TargetType.MACOS, TargetType.TVOS, TargetType.APPLE,
    TargetType.LINUX, TargetType.NATIVE, TargetType.COMMON -> Unit
}


internal fun TargetType.isDescendantOf(other: TargetType): Boolean =
    this == other || (parent != null && parent.isDescendantOf(other))

internal fun TargetType.isParentOf(other: TargetType) = other.isDescendantOf(this)

internal operator fun List<Target>.contains(type: TargetType): Boolean = any {  target -> target.type.isDescendantOf(type) }
