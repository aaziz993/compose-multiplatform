package gradle.plugins.kotlin

import gradle.api.applyTo
import org.gradle.kotlin.dsl.withType
import gradle.plugins.kotlin.mpp.KotlinAndroidTarget
import gradle.plugins.kotlin.targets.jvm.KotlinJvmTarget
import gradle.plugins.kotlin.targets.nat.KotlinNativeTargetImpl
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinTargetContainerWithPresetFunctions
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

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
            jvm.applyTo(receiver.targets.withType<org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget>()) { name, action ->
                receiver.jvm(name, action::execute)
            }
        }

        androidTarget?.forEach { androidTarget ->
            androidTarget.applyTo(receiver.targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget>()) { name, action ->
                receiver.androidTarget(name, action::execute)
            }
        }

        androidNativeX64?.forEach { androidNativeX64 ->
            androidNativeX64.applyTo(receiver.targets.withType<KotlinNativeTarget>()) { name, action ->
                receiver.androidNativeX64(name, action::execute)
            }
        }

        androidNativeX86?.forEach { androidNativeX86 ->
            androidNativeX86.applyTo(receiver.targets.withType<KotlinNativeTarget>()) { name, action ->
                receiver.androidNativeX86(name, action::execute)
            }
        }

        androidNativeArm32?.forEach { androidNativeArm32 ->
            androidNativeArm32.applyTo(receiver.targets.withType<KotlinNativeTarget>()) { name, action ->
                receiver.androidNativeArm32(name, action::execute)
            }
        }

        androidNativeArm64?.forEach { androidNativeArm64 ->
            androidNativeArm64.applyTo(receiver.targets.withType<KotlinNativeTarget>()) { name, action ->
                receiver.androidNativeArm64(name, action::execute)
            }
        }

        iosArm64?.forEach { iosArm64 ->
            iosArm64.applyTo(receiver.targets.withType<KotlinNativeTarget>()) { name, action ->
                receiver.iosArm64(name, action::execute)
            }
        }

        iosX64?.forEach { iosX64 ->
            iosX64.applyTo(receiver.targets.withType<KotlinNativeTarget>()) { name, action ->
                receiver.iosX64(name, action::execute)
            }
        }

        iosSimulatorArm64?.forEach { iosSimulatorArm64 ->
            iosSimulatorArm64.applyTo(receiver.targets.withType<KotlinNativeTarget>()) { name, action ->
                receiver.iosSimulatorArm64(name, action::execute)
            }
        }

        watchosArm32?.forEach { watchosArm32 ->
            watchosArm32.applyTo(receiver.targets.withType<KotlinNativeTarget>()) { name, action ->
                receiver.watchosArm32(name, action::execute)
            }
        }

        watchosArm64?.forEach { watchosArm64 ->
            watchosArm64.applyTo(receiver.targets.withType<KotlinNativeTarget>()) { name, action ->
                receiver.watchosArm64(name, action::execute)
            }
        }

        watchosX64?.forEach { watchosX64 ->
            watchosX64.applyTo(receiver.targets.withType<KotlinNativeTarget>()) { name, action ->
                receiver.watchosX64(name, action::execute)
            }
        }

        watchosSimulatorArm64?.forEach { watchosSimulatorArm64 ->
            watchosSimulatorArm64.applyTo(receiver.targets.withType<KotlinNativeTarget>()) { name, action ->
                receiver.watchosSimulatorArm64(name, action::execute)
            }
        }

        watchosDeviceArm64?.forEach { watchosDeviceArm64 ->
            watchosDeviceArm64.applyTo(receiver.targets.withType<KotlinNativeTarget>()) { name, action ->
                receiver.watchosDeviceArm64(name, action::execute)
            }
        }

        tvosArm64?.forEach { tvosArm64 ->
            tvosArm64.applyTo(receiver.targets.withType<KotlinNativeTarget>()) { name, action ->
                receiver.tvosArm64(name, action::execute)
            }
        }

        tvosX64?.forEach { tvosX64 ->
            tvosX64.applyTo(receiver.targets.withType<KotlinNativeTarget>()) { name, action ->
                receiver.tvosX64(name, action::execute)
            }
        }

        tvosSimulatorArm64?.forEach { tvosSimulatorArm64 ->
            tvosSimulatorArm64.applyTo(receiver.targets.withType<KotlinNativeTarget>()) { name, action ->
                receiver.tvosSimulatorArm64(name, action::execute)
            }
        }

        linuxX64?.forEach { linuxX64 ->
            linuxX64.applyTo(receiver.targets.withType<KotlinNativeTarget>()) { name, action ->
                receiver.linuxX64(name, action::execute)
            }
        }

        mingwX64?.forEach { mingwX64 ->
            mingwX64.applyTo(receiver.targets.withType<KotlinNativeTarget>()) { name, action ->
                receiver.mingwX64(name, action::execute)
            }
        }

        macosX64?.forEach { macosX64 ->
            macosX64.applyTo(receiver.targets.withType<KotlinNativeTarget>()) { name, action ->
                receiver.macosX64(name, action::execute)
            }
        }

        macosArm64?.forEach { macosArm64 ->
            macosArm64.applyTo(receiver.targets.withType<KotlinNativeTarget>()) { name, action ->
                receiver.macosArm64(name, action::execute)
            }
        }

        linuxArm64?.forEach { linuxArm64 ->
            linuxArm64.applyTo(receiver.targets.withType<KotlinNativeTarget>()) { name, action ->
                receiver.linuxArm64(name, action::execute)
            }
        }
    }
}
