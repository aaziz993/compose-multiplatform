package gradle

import org.gradle.api.NamedDomainObjectCollection
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.konan.target.Family
import org.jetbrains.kotlin.konan.target.KonanTarget

internal fun KotlinTarget.isNative(): Boolean = this is KotlinNativeTarget

internal fun KotlinNativeTarget.isIosSimulatorTarget(): Boolean =
    konanTarget === KonanTarget.IOS_X64 || konanTarget === KonanTarget.IOS_SIMULATOR_ARM64

internal fun KotlinNativeTarget.isIosDeviceTarget(): Boolean =
    konanTarget === KonanTarget.IOS_ARM64

internal fun KotlinNativeTarget.isIosTarget(): Boolean =
    konanTarget.family == Family.IOS

internal fun NamedDomainObjectCollection<KotlinTarget>.hasNative() = any(KotlinTarget::isNative)
