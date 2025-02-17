package gradle

import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.konan.target.KonanTarget
import org.gradle.api.NamedDomainObjectCollection

internal fun KotlinTarget.isNative(): Boolean = this is KotlinNativeTarget

internal fun KotlinNativeTarget.isIosSimulatorTarget(): Boolean =
    konanTarget === KonanTarget.IOS_X64 || konanTarget === KonanTarget.IOS_SIMULATOR_ARM64

internal fun KotlinNativeTarget.isIosDeviceTarget(): Boolean =
    konanTarget === KonanTarget.IOS_ARM64

internal fun KotlinNativeTarget.isIosTarget(): Boolean =
    isIosSimulatorTarget() || isIosDeviceTarget()

internal fun NamedDomainObjectCollection<KotlinTarget>.hasNative() = any(KotlinTarget::isNative)
