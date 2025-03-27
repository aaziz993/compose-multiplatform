package gradle.plugins.kotlin

import gradle.plugins.kotlin.mpp.KotlinAndroidTarget
import gradle.plugins.kotlin.targets.jvm.KotlinJvmTarget
import gradle.plugins.kotlin.targets.nat.KotlinNativeTargetImpl
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinTargetContainerWithPresetFunctions

internal interface KotlinTargetContainerWithPresetFunctions<T : KotlinTargetContainerWithPresetFunctions> :
    KotlinTargetsContainer<T> {

    val jvm: LinkedHashSet<KotlinJvmTarget>?

    val androidTarget: LinkedHashSet<KotlinAndroidTarget>?

    val androidNativeX64: LinkedHashSet<KotlinNativeTargetImpl>?

    val androidNativeX86: LinkedHashSet<KotlinNativeTargetImpl>?

    val androidNativeArm32: LinkedHashSet<KotlinNativeTargetImpl>?

    val androidNativeArm64: LinkedHashSet<KotlinNativeTargetImpl>?

    val iosArm64: LinkedHashSet<KotlinNativeTargetImpl>?

    val iosX64: LinkedHashSet<KotlinNativeTargetImpl>?

    val iosSimulatorArm64: LinkedHashSet<KotlinNativeTargetImpl>?

    val watchosArm32: LinkedHashSet<KotlinNativeTargetImpl>?

    val watchosArm64: LinkedHashSet<KotlinNativeTargetImpl>?

    val watchosX64: LinkedHashSet<KotlinNativeTargetImpl>?

    val watchosSimulatorArm64: LinkedHashSet<KotlinNativeTargetImpl>?

    val watchosDeviceArm64: LinkedHashSet<KotlinNativeTargetImpl>?

    val tvosArm64: LinkedHashSet<KotlinNativeTargetImpl>?

    val tvosX64: LinkedHashSet<KotlinNativeTargetImpl>?

    val tvosSimulatorArm64: LinkedHashSet<KotlinNativeTargetImpl>?

    val linuxX64: LinkedHashSet<KotlinNativeTargetImpl>?

    val mingwX64: LinkedHashSet<KotlinNativeTargetImpl>?

    val macosX64: LinkedHashSet<KotlinNativeTargetImpl>?

    val macosArm64: LinkedHashSet<KotlinNativeTargetImpl>?

    val linuxArm64: LinkedHashSet<KotlinNativeTargetImpl>?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        jvm?.forEach { jvm ->
            receiver.jvm(jvm.name) {
                jvm.applyTo(this)
            }
        }

        androidTarget?.forEach { androidTarget ->
            receiver.androidTarget(androidTarget.name) {
                androidTarget.applyTo(this)
            }
        }

        androidNativeX64?.forEach { androidNativeX64 ->
            receiver.androidNativeX64(androidNativeX64.name) {
                androidNativeX64.applyTo(this)
            }
        }

        androidNativeX86?.forEach { androidNativeX86 ->
            receiver.androidNativeX86(androidNativeX86.name) {
                androidNativeX86.applyTo(this)
            }
        }

        androidNativeArm32?.forEach { androidNativeArm32 ->
            receiver.androidNativeArm32(androidNativeArm32.name) {
                androidNativeArm32.applyTo(this)
            }
        }

        androidNativeArm64?.forEach { androidNativeArm64 ->
            receiver.androidNativeArm64(androidNativeArm64.name) {
                androidNativeArm64.applyTo(this)
            }
        }

        iosArm64?.forEach { iosArm64 ->
            receiver.iosArm64(iosArm64.name) {
                iosArm64.applyTo(this)
            }
        }

        iosX64?.forEach { iosX64 ->
            receiver.iosX64(iosX64.name) {
                iosX64.applyTo(this)
            }
        }

        iosSimulatorArm64?.forEach { iosSimulatorArm64 ->
            receiver.iosSimulatorArm64(iosSimulatorArm64.name) {
                iosSimulatorArm64.applyTo(this)
            }
        }

        watchosArm32?.forEach { watchosArm32 ->
            receiver.watchosArm32(watchosArm32.name) {
                watchosArm32.applyTo(this)
            }
        }

        watchosArm64?.forEach { watchosArm64 ->
            receiver.watchosArm64(watchosArm64.name) {
                watchosArm64.applyTo(this)
            }
        }

        watchosX64?.forEach { watchosX64 ->
            receiver.watchosX64(watchosX64.name) {
                watchosX64.applyTo(this)
            }
        }

        watchosSimulatorArm64?.forEach { watchosSimulatorArm64 ->
            receiver.watchosSimulatorArm64(watchosSimulatorArm64.name) {
                watchosSimulatorArm64.applyTo(this)
            }
        }

        watchosDeviceArm64?.forEach { watchosDeviceArm64 ->
            receiver.watchosDeviceArm64(watchosDeviceArm64.name) {
                watchosDeviceArm64.applyTo(this)
            }
        }

        tvosArm64?.forEach { tvosArm64 ->
            receiver.tvosArm64(tvosArm64.name) {
                tvosArm64.applyTo(this)
            }
        }

        tvosX64?.forEach { tvosX64 ->
            receiver.tvosX64(tvosX64.name) {
                tvosX64.applyTo(this)
            }
        }

        tvosSimulatorArm64?.forEach { tvosSimulatorArm64 ->
            receiver.tvosSimulatorArm64(tvosSimulatorArm64.name) {
                tvosSimulatorArm64.applyTo(this)
            }
        }

        linuxX64?.forEach { linuxX64 ->
            receiver.linuxX64(linuxX64.name) {
                linuxX64.applyTo(this)
            }
        }

        mingwX64?.forEach { mingwX64 ->
            receiver.mingwX64(mingwX64.name) {
                mingwX64.applyTo(this)
            }
        }

        macosX64?.forEach { macosX64 ->
            receiver.macosX64(macosX64.name) {
                macosX64.applyTo(this)
            }
        }

        macosArm64?.forEach { macosArm64 ->
            receiver.macosArm64(macosArm64.name) {
                macosArm64.applyTo(this)
            }
        }

        linuxArm64?.forEach { linuxArm64 ->
            receiver.linuxArm64(linuxArm64.name) {
                linuxArm64.applyTo(this)
            }
        }
    }
}
