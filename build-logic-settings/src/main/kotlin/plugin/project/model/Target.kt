package plugin.project.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmJsTargetDsl
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

@Serializable
internal sealed class Target {

    abstract val name: String?

    @Serializable
    @SerialName("jvm")
    data class Jvm(override val name: String? = null) : Target() {

        fun applyTo(extension: KotlinMultiplatformExtension, configure: KotlinJvmTarget.() -> Unit = {}) =
            with(extension) {
                name?.let { jvm(it, configure) } ?: jvm(configure)
            }
    }

    @Serializable
    @SerialName("android")
    data class Android(override val name: String? = null) : Target() {

        fun applyTo(extension: KotlinMultiplatformExtension, configure: KotlinAndroidTarget.() -> Unit = {}) =
            with(extension) {
                name?.let { androidTarget(it, configure) } ?: androidTarget(configure)
            }
    }

    @Serializable
    @SerialName("native")
    data class Native(override val name: String? = null) : Target()

    @Serializable
    @SerialName("androidNative")
    data class AndroidNative(override val name: String? = null) : Target()

    @Serializable
    @SerialName("androidNativeArm32")
    data class AndroidNativeArm32(override val name: String? = null) : Target() {

        fun applyTo(extension: KotlinMultiplatformExtension, configure: KotlinNativeTarget.() -> Unit = {}) =
            with(extension) {
                name?.let { androidNativeArm32(it, configure) } ?: mingwX64(configure)
            }
    }

    @Serializable
    @SerialName("androidNativeArm64")
    data class AndroidNativeArm64(override val name: String? = null) : Target() {

        fun applyTo(extension: KotlinMultiplatformExtension, configure: KotlinNativeTarget.() -> Unit = {}) =
            with(extension) {
                name?.let { androidNativeArm64(it, configure) } ?: androidNativeArm64(configure)
            }
    }

    @Serializable
    @SerialName("androidNativeX86")
    data class AndroidNativeX86(override val name: String? = null) : Target() {

        fun applyTo(extension: KotlinMultiplatformExtension, configure: KotlinNativeTarget.() -> Unit = {}) =
            with(extension) {
                name?.let { androidNativeX86(it, configure) } ?: androidNativeX86(configure)
            }
    }

    @Serializable
    @SerialName("androidNativeX64")
    data class AndroidNativeX64(override val name: String? = null) : Target() {

        fun applyTo(extension: KotlinMultiplatformExtension, configure: KotlinNativeTarget.() -> Unit = {}) =
            with(extension) {
                name?.let { androidNativeX64(it, configure) } ?: androidNativeX64(configure)
            }
    }

    @Serializable
    @SerialName("apple")
    data class Apple(override val name: String? = null) : Target()

    @Serializable
    @SerialName("ios")
    data class Ios(override val name: String? = null) : Target()

    @Serializable
    @SerialName("iosArm64")
    data class IosArm64(override val name: String? = null) : Target() {

        fun applyTo(extension: KotlinMultiplatformExtension, configure: KotlinNativeTarget.() -> Unit = {}) =
            with(extension) {
                name?.let { iosArm64(it, configure) } ?: iosArm64(configure)
            }
    }

    @Serializable
    @SerialName("iosX64")
    data class IosX64(override val name: String? = null) : Target() {

        fun applyTo(extension: KotlinMultiplatformExtension, configure: KotlinNativeTarget.() -> Unit = {}) =
            with(extension) {
                name?.let { iosX64(it, configure) } ?: iosX64(configure)
            }
    }

    @Serializable
    @SerialName("iosSimulatorArm64")
    data class IosSimulatorArm64(override val name: String? = null) : Target() {

        fun applyTo(extension: KotlinMultiplatformExtension, configure: KotlinNativeTarget.() -> Unit = {}) =
            with(extension) {
                name?.let { iosSimulatorArm64(it, configure) } ?: iosSimulatorArm64(configure)
            }
    }

    @Serializable
    @SerialName("watchos")
    data class Watchos(override val name: String? = null) : Target()

    @Serializable
    @SerialName("watchosArm32")
    data class WatchosArm32(override val name: String? = null) : Target() {

        fun applyTo(extension: KotlinMultiplatformExtension, configure: KotlinNativeTarget.() -> Unit = {}) =
            with(extension) {
                name?.let { watchosArm32(it, configure) } ?: watchosArm32(configure)
            }
    }

    @Serializable
    @SerialName("watchosArm64")
    data class WatchosArm64(override val name: String? = null) : Target() {

        fun applyTo(extension: KotlinMultiplatformExtension, configure: KotlinNativeTarget.() -> Unit = {}) =
            with(extension) {
                name?.let { watchosArm64(it, configure) } ?: watchosArm64(configure)
            }
    }

    @Serializable
    @SerialName("watchosDeviceArm64")
    data class WatchosDeviceArm64(override val name: String? = null) : Target() {

        fun applyTo(extension: KotlinMultiplatformExtension, configure: KotlinNativeTarget.() -> Unit = {}) =
            with(extension) {
                name?.let { watchosDeviceArm64(it, configure) } ?: watchosDeviceArm64(configure)
            }
    }

    @Serializable
    @SerialName("watchosSimulatorArm64")
    data class WatchosX64(override val name: String? = null) : Target() {

        fun applyTo(extension: KotlinMultiplatformExtension, configure: KotlinNativeTarget.() -> Unit = {}) =
            with(extension) {
                name?.let { watchosX64(it, configure) } ?: watchosX64(configure)
            }
    }

    @Serializable
    @SerialName("watchosSimulatorArm64")
    data class WatchosSimulatorArm64(override val name: String? = null) : Target() {

        fun applyTo(extension: KotlinMultiplatformExtension, configure: KotlinNativeTarget.() -> Unit = {}) =
            with(extension) {
                name?.let { watchosSimulatorArm64(it, configure) } ?: watchosSimulatorArm64(configure)
            }
    }

    @Serializable
    @SerialName("macos")
    data class Macos(override val name: String? = null) : Target()

    @Serializable
    @SerialName("macosArm64")
    data class MacosArm64(override val name: String? = null) : Target() {

        fun applyTo(extension: KotlinMultiplatformExtension, configure: KotlinNativeTarget.() -> Unit = {}) =
            with(extension) {
                name?.let { macosArm64(it, configure) } ?: macosArm64(configure)
            }
    }

    @Serializable
    @SerialName("macosX64")
    data class MacosX64(override val name: String? = null) : Target() {

        fun applyTo(extension: KotlinMultiplatformExtension, configure: KotlinNativeTarget.() -> Unit = {}) =
            with(extension) {
                name?.let { macosX64(it, configure) } ?: tvosArm64(configure)
            }
    }

    @Serializable
    @SerialName("tvos")
    data class Tvos(override val name: String? = null) : Target()

    @Serializable
    @SerialName("tvosArm64")
    data class TvosArm64(override val name: String? = null) : Target() {

        fun applyTo(extension: KotlinMultiplatformExtension, configure: KotlinNativeTarget.() -> Unit = {}) =
            with(extension) {
                name?.let { tvosArm64(it, configure) } ?: tvosArm64(configure)
            }
    }

    @Serializable
    @SerialName("tvosX64")
    data class TvosX64(override val name: String? = null) : Target() {

        fun applyTo(extension: KotlinMultiplatformExtension, configure: KotlinNativeTarget.() -> Unit = {}) =
            with(extension) {
                name?.let { tvosX64(it, configure) } ?: tvosX64(configure)
            }
    }

    @Serializable
    @SerialName("tvosSimulatorArm64")
    data class TvosSimulatorArm64(override val name: String? = null) : Target() {

        fun applyTo(extension: KotlinMultiplatformExtension, configure: KotlinNativeTarget.() -> Unit = {}) =
            with(extension) {
                name?.let { tvosSimulatorArm64(it, configure) } ?: tvosSimulatorArm64(configure)
            }
    }

    @Serializable
    @SerialName("linux")
    data class Linux(override val name: String? = null) : Target()

    @Serializable
    @SerialName("linuxX64")
    data class LinuxX64(override val name: String? = null) : Target() {

        fun applyTo(extension: KotlinMultiplatformExtension, configure: KotlinNativeTarget.() -> Unit = {}) =
            with(extension) {
                name?.let { linuxArm64(it, configure) } ?: linuxArm64(configure)
            }
    }

    @Serializable
    @SerialName("linuxArm64")
    data class LinuxArm64(override val name: String? = null) : Target() {

        fun applyTo(extension: KotlinMultiplatformExtension, configure: KotlinNativeTarget.() -> Unit = {}) =
            with(extension) {
                name?.let { linuxX64(it, configure) } ?: linuxX64(configure)
            }
    }

    @Serializable
    @SerialName("mingw")
    data class Mingw(override val name: String? = null) : Target()

    @Serializable
    @SerialName("mingwX64")
    data class MingwX64(override val name: String? = null) : Target() {

        fun applyTo(extension: KotlinMultiplatformExtension, configure: KotlinNativeTarget.() -> Unit = {}) =
            with(extension) {
                name?.let { mingwX64(it, configure) } ?: mingwX64(configure)
            }
    }

    @Serializable
    @SerialName("js")
    data class Js(override val name: String? = null) : Target() {

        fun applyTo(extension: KotlinMultiplatformExtension, configure: KotlinJsTargetDsl.() -> Unit = {}) =
            with(extension) {
                name?.let { js(it, configure) } ?: js(configure)
            }
    }

    @Serializable
    @SerialName("wasm")
    data class WasmJs(override val name: String? = null) : Target() {

        fun applyTo(extension: KotlinMultiplatformExtension, configure: KotlinWasmJsTargetDsl.() -> Unit = {}) =
            with(extension) {
                name?.let { wasmJs(it, configure) } ?: wasmJs(configure)
            }
    }

    val isAndroidNative by lazy {
        when (this) {
            is AndroidNative, is AndroidNativeArm32, is AndroidNativeArm64, is AndroidNativeX86, is AndroidNativeX64 -> true
            else -> false
        }
    }

    val isIos by lazy {
        when (this) {
            is Ios, is IosArm64, is IosX64, is IosSimulatorArm64 -> true
            else -> false
        }
    }

    val isWatchos by lazy {
        when (this) {
            is Watchos, is WatchosDeviceArm64, is WatchosArm32, is WatchosArm64, is WatchosSimulatorArm64 -> true
            else -> false
        }
    }

    val isTvos by lazy {
        when (this) {
            is Tvos, is TvosArm64, is TvosX64, is TvosSimulatorArm64 -> true
            else -> false
        }
    }

    val isMacos by lazy {
        when (this) {
            is Macos, is MacosArm64, is MacosX64 -> true
            else -> false
        }
    }

    val isApple by lazy {
        isIos || isWatchos || isTvos || isMacos
    }

    val isLinux by lazy {
        when (this) {
            is Linux, is LinuxArm64 -> true
            else -> false
        }
    }

    val isWindows by lazy {
        when (this) {
            is Mingw, is MingwX64 -> true
            else -> false
        }
    }

    val isNative by lazy {
        isAndroidNative || isApple || isLinux || isWindows
    }
}

internal val List<Target>.hasJvm
    get() = any { it is Target.Jvm }

@Suppress("UNCHECKED_CAST")
internal val List<Target>.jvm
    get() = filter { it is Target.AndroidNativeArm32 } as List<Target.Android>

internal val List<Target>.hasAndroid
    get() = any { it is Target.Android }

@Suppress("UNCHECKED_CAST")
internal val List<Target>.android
    get() = filter { it is Target.Android } as List<Target.Android>

internal val List<Target>.hasNative
    get() = any(Target::isNative)

internal val List<Target>.hasAndroidNative
    get() = any(Target::isAndroidNative)

@Suppress("UNCHECKED_CAST")
internal val List<Target>.androidNativeArm32
    get() = filter { it is Target.AndroidNativeArm32 } as List<Target.AndroidNativeArm32>

@Suppress("UNCHECKED_CAST")
internal val List<Target>.androidNativeArm64
    get() = filter { it is Target.AndroidNativeArm64 } as List<Target.AndroidNativeArm64>

@Suppress("UNCHECKED_CAST")
internal val List<Target>.androidNativeX86
    get() = filter { it is Target.AndroidNativeX86 } as List<Target.AndroidNativeX86>

@Suppress("UNCHECKED_CAST")
internal val List<Target>.androidNativeX64
    get() = filter { it is Target.AndroidNativeX64 } as List<Target.AndroidNativeX64>

internal val List<Target>.hasIos
    get() = any(Target::isIos)

@Suppress("UNCHECKED_CAST")
internal val List<Target>.iosArm64
    get() = filter { it is Target.IosArm64 } as List<Target.IosArm64>

@Suppress("UNCHECKED_CAST")
internal val List<Target>.iosX64
    get() = filter { it is Target.IosX64 } as List<Target.IosX64>

@Suppress("UNCHECKED_CAST")
internal val List<Target>.iosSimulatorArm64
    get() = filter { it is Target.IosSimulatorArm64 } as List<Target.IosSimulatorArm64>

internal val List<Target>.hasWatchos
    get() = any(Target::isWatchos)

@Suppress("UNCHECKED_CAST")
internal val List<Target>.watchosArm32
    get() = filter { it is Target.WatchosArm32 } as List<Target.WatchosArm32>

@Suppress("UNCHECKED_CAST")
internal val List<Target>.watchosArm64
    get() = filter { it is Target.WatchosArm64 } as List<Target.WatchosArm64>


@Suppress("UNCHECKED_CAST")
internal val List<Target>.watchosDeviceArm64
    get() = filter { it is Target.WatchosDeviceArm64 } as List<Target.WatchosDeviceArm64>

@Suppress("UNCHECKED_CAST")
internal val List<Target>.watchosX64
    get() = filter { it is Target.WatchosX64 } as List<Target.WatchosX64>

@Suppress("UNCHECKED_CAST")
internal val List<Target>.watchosSimulatorArm64
    get() = filter { it is Target.WatchosSimulatorArm64 } as List<Target.WatchosSimulatorArm64>

internal val List<Target>.hasTvos
    get() = any(Target::isTvos)

@Suppress("UNCHECKED_CAST")
internal val List<Target>.tvosArm64
    get() = filter { it is Target.TvosArm64 } as List<Target.WatchosSimulatorArm64>

@Suppress("UNCHECKED_CAST")
internal val List<Target>.tvosX64
    get() = filter { it is Target.TvosX64 } as List<Target.TvosX64>

@Suppress("UNCHECKED_CAST")
internal val List<Target>.tvosSimulatorArm64
    get() = filter { it is Target.TvosSimulatorArm64 } as List<Target.TvosSimulatorArm64>


internal val List<Target>.hasMacos
    get() = any(Target::isMacos)

@Suppress("UNCHECKED_CAST")
internal val List<Target>.macosArm64
    get() = filter { it is Target.MacosArm64 } as List<Target.MacosArm64>

@Suppress("UNCHECKED_CAST")
internal val List<Target>.macosX64
    get() = filter { it is Target.MacosX64 } as List<Target.MacosX64>

internal val List<Target>.hasApple
    get() = any(Target::isApple)

internal val List<Target>.hasLinux
    get() = any(Target::isLinux)

@Suppress("UNCHECKED_CAST")
internal val List<Target>.linuxArm64
    get() = filter { it is Target.LinuxArm64 } as List<Target.LinuxArm64>

@Suppress("UNCHECKED_CAST")
internal val List<Target>.linuxX64
    get() = filter { it is Target.LinuxX64 } as List<Target.LinuxX64>

internal val List<Target>.hasWindows
    get() = any(Target::isWindows)

@Suppress("UNCHECKED_CAST")
internal val List<Target>.mingwX64
    get() = filter { it is Target.MingwX64 } as List<Target.MingwX64>

internal val List<Target>.hasJs
    get() = any { target -> target is Target.Js }

@Suppress("UNCHECKED_CAST")
internal val List<Target>.js
    get() = filter { it is Target.Js } as List<Target.Js>

internal val List<Target>.hasWasmJs
    get() = any { target -> target is Target.WasmJs }

@Suppress("UNCHECKED_CAST")
internal val List<Target>.wasmjs
    get() = filter { it is Target.WasmJs } as List<Target.WasmJs>

