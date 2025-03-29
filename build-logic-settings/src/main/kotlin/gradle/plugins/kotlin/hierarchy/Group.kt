package gradle.plugins.kotlin.hierarchy

import gradle.api.BaseNamed
import gradle.api.NamedObjectTransformingSerializer
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable

@KeepGeneratedSerializer
@Serializable(with = GroupObjectTransformingSerializer::class)
internal data class Group(
    override val name: String,
    override val groups: Set<Group>? = null,
    override val common: KotlinHierarchyBuilderImpl? = null,
    override val withCompilations: Set<String>? = null,
    override val excludeCompilations: Set<String>? = null,
    override val withNative: Boolean? = null,
    override val withApple: Boolean? = null,
    override val withIos: Boolean? = null,
    override val withWatchos: Boolean? = null,
    override val withMacos: Boolean? = null,
    override val withTvos: Boolean? = null,
    override val withMingw: Boolean? = null,
    override val withLinux: Boolean? = null,
    override val withAndroidNative: Boolean? = null,
    override val withJs: Boolean? = null,
    override val withWasmJs: Boolean? = null,
    override val withWasmWasi: Boolean? = null,
    override val withJvm: Boolean? = null,
    override val withAndroidTarget: Boolean? = null,
    override val withAndroidNativeX64: Boolean? = null,
    override val withAndroidNativeX86: Boolean? = null,
    override val withAndroidNativeArm32: Boolean? = null,
    override val withAndroidNativeArm64: Boolean? = null,
    override val withIosArm64: Boolean? = null,
    override val withIosX64: Boolean? = null,
    override val withIosSimulatorArm64: Boolean? = null,
    override val withWatchosArm32: Boolean? = null,
    override val withWatchosArm64: Boolean? = null,
    override val withWatchosX64: Boolean? = null,
    override val withWatchosSimulatorArm64: Boolean? = null,
    override val withWatchosDeviceArm64: Boolean? = null,
    override val withTvosArm64: Boolean? = null,
    override val withTvosX64: Boolean? = null,
    override val withTvosSimulatorArm64: Boolean? = null,
    override val withLinuxX64: Boolean? = null,
    override val withMingwX64: Boolean? = null,
    override val withMacosX64: Boolean? = null,
    override val withMacosArm64: Boolean? = null,
    override val withLinuxArm64: Boolean? = null,
) : KotlinHierarchyBuilder<org.jetbrains.kotlin.gradle.plugin.KotlinHierarchyBuilder>, BaseNamed

private object GroupObjectTransformingSerializer :
    NamedObjectTransformingSerializer<Group>(Group.generatedSerializer())
