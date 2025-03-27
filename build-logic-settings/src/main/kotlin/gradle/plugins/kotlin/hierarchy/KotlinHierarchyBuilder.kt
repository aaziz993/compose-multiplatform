package gradle.plugins.kotlin.hierarchy

import gradle.actIfTrue
import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

internal interface KotlinHierarchyBuilder<T : org.jetbrains.kotlin.gradle.plugin.KotlinHierarchyBuilder> {
    /**
     * The root node in the hierarchy DSL structure.
     */
    @Serializable
    data class Root(
        override val groups: Set<@Serializable(with = GroupKeyTransformingSerializer::class) Group>? = null,
        override val common: KotlinHierarchyBuilder<out org.jetbrains.kotlin.gradle.plugin.KotlinHierarchyBuilder>? = null,
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
        /**
         * Defines the [KotlinSourceSetTrees][KotlinSourceSetTree] that the described hierarchy is applied to.
         *
         * Examples of DSL:
         *
         * 1. Only apply a hierarchy for the "main" and "test" [KotlinSourceSetTree]:
         *
         * ```kotlin
         * applyHierarchyTemplate {
         *     sourceSetTrees(KotlinSourceSetTree.main, KotlinSourceSetTree.test)
         *     common {
         *         withJvm()
         *         group("ios") {
         *             withIos()
         *         }
         *     }
         * }
         *```
         *
         * When given the `iosX64()`, `iosArm64()`, and `jvm()` targets, this DSL creates the following trees:
         * ```
         *             "main"                      "test"
         *           commonMain                 commonTest
         *                |                          |
         *           +----+-----+               +----+-----+
         *           |          |               |          |
         *        iosMain    jvmMain         iosTest    jvmTest
         *           |                          |
         *       +---+----+                 +---+----+
         *       |        |                 |        |
         * iosX64Main  iosArm64Main   iosX64Test  iosArm64Test
         * ```
         *
         * 2. Use a different hierarchy for "main" and "test" [KotlinSourceSetTree]:
         *
         *```kotlin
         * applyHierarchyTemplate {
         *    sourceSetTrees(SourceSetTree.main)  // ! <- only applied to the "main" tree
         *    common {
         *        withJvm()
         *        group("ios") {
         *            withIos()
         *        }
         *    }
         * }
         *
         * applyHierarchyTemplate {
         *     sourceSetTrees(SourceSetTree.test) // ! <- only applied to the "test" tree
         *     common {
         *         withJvm()
         *         withIos()
         *     }
         * }
         * ```
         *
         * When given the `iosX64()`, `iosArm64()`, and `jvm()` targets, this DSL creates the following trees:
         * ```
         *             "main"                            "test"
         *           commonMain                        commonTest
         *                |                                 |
         *           +----+-----+               +-----------+-----------+
         *           |          |               |           |           |
         *        iosMain    jvmMain      iosX64Test   iosArm64Test  jvmTest
         *           |
         *       +---+----+
         *       |        |
         * iosX64Main  iosArm64Main
         * ```
         */
        val sourceSetTrees: Set<String>? = null,
        /**
         * Adds the given [trees][tree] into this descriptor.
         *
         * @see sourceSetTrees
         */
        val withSourceSetTree: Set<String>? = null,
        /**
         * Removes the given [trees][tree] from this descriptor.
         * @see sourceSetTrees
         */
        val excludeSourceSetTree: Set<String>? = null,
    ) : KotlinHierarchyBuilder<org.jetbrains.kotlin.gradle.plugin.KotlinHierarchyBuilder.Root> {

        override fun applyTo(receiver: org.jetbrains.kotlin.gradle.plugin.KotlinHierarchyBuilder.Root) {
            super.applyTo(receiver)

            sourceSetTrees?.map(::KotlinSourceSetTree)?.toTypedArray()?.let(receiver::sourceSetTrees)
            withSourceSetTree?.map(::KotlinSourceSetTree)?.toTypedArray()?.let(receiver::withSourceSetTree)
            excludeSourceSetTree?.map(::KotlinSourceSetTree)?.toTypedArray()?.let(receiver::excludeSourceSetTree)
        }
    }

    /**
     * Creates a group of [KotlinSourceSets][KotlinSourceSet] with the given [name] and structure provided via the [build] block.
     *
     * @see common
     */
    val groups: Set<@Serializable(with = GroupKeyTransformingSerializer::class) Group>?

    /**
     * Creates a group with the name "common". It's a shortcut for `group("common") { }`.
     *
     * Most hierarchies attach their nodes/groups to "common".
     *
     * The following example applies the shown hierarchy to the "main" compilations and creates a `nativeMain` source set
     * that depends on the usual 'commonMain' source set:
     * ```
     * common {
     *     group("native") {
     *         withIos()
     *         withMacos()
     *     }
     * }
     * ```
     */
    val common: KotlinHierarchyBuilder<out org.jetbrains.kotlin.gradle.plugin.KotlinHierarchyBuilder>?

    /**
     * Allows including only those [KotlinCompilations][gradle.plugins.kotlin.KotlinCompilation] for which the [predicate] returns `true`.
     *
     * This is a low-level API. Try to avoid using it.
     */
    val withCompilations: Set<String>?

    /**
     * Allows including only those [KotlinCompilations][gradle.plugins.kotlin.KotlinCompilation] for which the [predicate] returns `true`.
     *
     * This is a low-level API. Try to avoid using it.
     */
    val excludeCompilations: Set<String>?

    /**
     * Only includes targets for Kotlin/Native in this [group].
     *
     * For more information, see [Native targets overview](https://kotlinlang.org/docs/native-target-support.html).
     */
    val withNative: Boolean?

    /**
     * Only includes Kotlin's Apple targets in this [group].
     *
     * For more information, see [Native targets overview](https://kotlinlang.org/docs/native-target-support.html).
     */
    val withApple: Boolean?

    /**
     * Only includes Kotlin's Apple/iOS targets in this [group].
     *
     * For more information, see [Native targets overview](https://kotlinlang.org/docs/native-target-support.html).
     */
    val withIos: Boolean?

    /**
     * Only includes Kotlin's Apple/watchOS targets in this [group].
     *
     * For more information, see [Native targets overview](https://kotlinlang.org/docs/native-target-support.html).
     */
    val withWatchos: Boolean?

    /**
     * Only includes Kotlin's Apple/macOS targets in this [group].
     *
     * For more information, see [Native targets overview](https://kotlinlang.org/docs/native-target-support.html).
     */
    val withMacos: Boolean?

    /**
     * Only includes Kotlin's Apple/tvOS targets in this [group].
     *
     * For more information, see [Native targets overview](https://kotlinlang.org/docs/native-target-support.html).
     */
    val withTvos: Boolean?

    /**
     * Only includes Kotlin's MinGW targets in this [group].
     *
     * For more information, see [Native targets overview](https://kotlinlang.org/docs/native-target-support.html).
     */
    val withMingw: Boolean?

    /**
     * Only includes Kotlin's Linux targets in this [group].
     *
     * For more information, see [Native targets overview](https://kotlinlang.org/docs/native-target-support.html).
     */
    val withLinux: Boolean?

    /**
     * Only includes Kotlin's Android/Native targets in this [group].
     *
     * For more information, see [Native targets overview](https://kotlinlang.org/docs/native-target-support.html).
     */
    val withAndroidNative: Boolean?

    /**
     * Only includes targets for Kotlin/JS in this [group].
     */
    val withJs: Boolean?

    /**
     * Only includes Kotlin's Wasm/JS targets in this [group].
     */
    val withWasmJs: Boolean?

    /**
     * Only includes Kotlin's Wasm/WASI targets in this [group].
     */
    val withWasmWasi: Boolean?

    /**
     * Only includes targets for Kotlin/JVM in this [group].
     */
    val withJvm: Boolean?

    /**
     * Only includes Kotlin's Android targets in this [group].
     */
    val withAndroidTarget: Boolean?

    /**
     * Only includes Kotlin's Android/androidNativeX64 target in this [group].
     *
     * For more information, see [Native targets overview](https://kotlinlang.org/docs/native-target-support.html).
     */
    val withAndroidNativeX64: Boolean?

    /**
     * Only includes Kotlin's Android/androidNativeX86 target in this [group].
     *
     * For more information, see [Native targets overview](https://kotlinlang.org/docs/native-target-support.html).
     */
    val withAndroidNativeX86: Boolean?

    /**
     * Only includes Kotlin's Android/androidNativeArm32 target in this [group].
     *
     * For more information, see [Native targets overview](https://kotlinlang.org/docs/native-target-support.html).
     */
    val withAndroidNativeArm32: Boolean?

    /**
     * Only includes Kotlin's Android/androidNativeArm64 target in this [group].
     *
     * For more information, see [Native targets overview](https://kotlinlang.org/docs/native-target-support.html).
     */
    val withAndroidNativeArm64: Boolean?

    /**
     * Only includes Kotlin's Apple/iosArm64 target in this [group].
     *
     * For more information, see [Native targets overview](https://kotlinlang.org/docs/native-target-support.html).
     */
    val withIosArm64: Boolean?

    /**
     * Only includes Kotlin's Apple/iosX64 target in this [group].
     *
     * For more information, see [Native targets overview](https://kotlinlang.org/docs/native-target-support.html).
     */
    val withIosX64: Boolean?

    /**
     * Only includes Kotlin's Apple/iosSimulatorArm64 target in this [group].
     *
     * For more information, see [Native targets overview](https://kotlinlang.org/docs/native-target-support.html).
     */
    val withIosSimulatorArm64: Boolean?

    /**
     * Only includes Kotlin's Apple/watchosArm32 target in this [group].
     *
     * For more information, see [Native targets overview](https://kotlinlang.org/docs/native-target-support.html).
     */
    val withWatchosArm32: Boolean?

    /**
     * Only includes Kotlin's Apple/watchosArm64 target in this [group].
     *
     * For more information, see [Native targets overview](https://kotlinlang.org/docs/native-target-support.html).
     */
    val withWatchosArm64: Boolean?

    /**
     * Only includes Kotlin's Apple/watchosX64 target in this [group].
     *
     * For more information, see [Native targets overview](https://kotlinlang.org/docs/native-target-support.html).
     */
    val withWatchosX64: Boolean?

    /**
     * Only includes Kotlin's Apple/watchosSimulatorArm64 target in this [group].
     *
     * For more information, see [Native targets overview](https://kotlinlang.org/docs/native-target-support.html).
     */
    val withWatchosSimulatorArm64: Boolean?

    /**
     * Only includes Kotlin's Apple/watchosDeviceArm64 target in this [group].
     *
     * For more information, see [Native targets overview](https://kotlinlang.org/docs/native-target-support.html).
     */
    val withWatchosDeviceArm64: Boolean?

    /**
     * Only includes Kotlin's Apple/tvosArm64 target in this [group].
     *
     * For more information, see [Native targets overview](https://kotlinlang.org/docs/native-target-support.html).
     */
    val withTvosArm64: Boolean?

    /**
     * Only includes Kotlin's Apple/tvosX64 target in this [group].
     *
     * For more information, see [Native targets overview](https://kotlinlang.org/docs/native-target-support.html).
     */
    val withTvosX64: Boolean?

    /**
     * Only includes Kotlin's Apple/tvosSimulatorArm64 target in this [group].
     *
     * For more information, see [Native targets overview](https://kotlinlang.org/docs/native-target-support.html).
     */
    val withTvosSimulatorArm64: Boolean?

    /**
     * Only includes Kotlin's linuxX64 target in this [group].
     *
     * For more information, see [Native targets overview](https://kotlinlang.org/docs/native-target-support.html).
     */
    val withLinuxX64: Boolean?

    /**
     * Only includes Kotlin's mingwX64 target in this [group].
     *
     * For more information, see [Native targets overview](https://kotlinlang.org/docs/native-target-support.html).
     */
    val withMingwX64: Boolean?

    /**
     * Only includes Kotlin's Apple/macosX64 target in this [group].
     *
     * For more information, see [Native targets overview](https://kotlinlang.org/docs/native-target-support.html).
     */
    val withMacosX64: Boolean?

    /**
     * Only includes Kotlin's Apple/macosArm64 target in this [group].
     *
     * For more information, see [Native targets overview](https://kotlinlang.org/docs/native-target-support.html).
     */
    val withMacosArm64: Boolean?

    /**
     * Only includes Kotlin's linuxArm64 target in this [group].
     *
     * For more information, see [Native targets overview](https://kotlinlang.org/docs/native-target-support.html).
     */
    val withLinuxArm64: Boolean?

    fun applyTo(receiver: T) {
        groups?.forEach { group ->
            receiver.group(group.name, group::applyTo)
        }

        common?.let { common ->
            receiver.common((common as KotlinHierarchyBuilder<org.jetbrains.kotlin.gradle.plugin.KotlinHierarchyBuilder>)::applyTo)
        }

        withCompilations?.let { withCompilations ->
            receiver.withCompilations { compilation ->
                compilation.name in withCompilations
            }
        }

        excludeCompilations?.let { excludeCompilations ->
            receiver.excludeCompilations { compilation ->
                compilation.name in excludeCompilations
            }
        }

        withNative?.actIfTrue(receiver::withNative)
        withApple?.actIfTrue(receiver::withApple)
        withIos?.actIfTrue(receiver::withIos)
        withWatchos?.actIfTrue(receiver::withWatchos)
        withMacos?.actIfTrue(receiver::withMacos)
        withTvos?.actIfTrue(receiver::withTvos)
        withMingw?.actIfTrue(receiver::withMingw)
        withLinux?.actIfTrue(receiver::withLinux)
        withAndroidNative?.actIfTrue(receiver::withAndroidNative)
        withJs?.actIfTrue(receiver::withJs)
        withWasmJs?.actIfTrue(receiver::withWasmJs)
        withWasmWasi?.actIfTrue(receiver::withWasmWasi)
        withJvm?.actIfTrue(receiver::withJvm)
        withAndroidTarget?.actIfTrue(receiver::withAndroidTarget)
        withAndroidNativeX64?.actIfTrue(receiver::withAndroidNativeX64)
        withAndroidNativeX86?.actIfTrue(receiver::withAndroidNativeX86)
        withAndroidNativeArm32?.actIfTrue(receiver::withAndroidNativeArm32)
        withAndroidNativeArm64?.actIfTrue(receiver::withAndroidNativeArm64)
        withIosArm64?.actIfTrue(receiver::withIosArm64)
        withIosX64?.actIfTrue(receiver::withIosX64)
        withIosSimulatorArm64?.actIfTrue(receiver::withIosSimulatorArm64)
        withWatchosArm32?.actIfTrue(receiver::withWatchosArm32)
        withWatchosArm64?.actIfTrue(receiver::withWatchosArm64)
        withWatchosX64?.actIfTrue(receiver::withWatchosX64)
        withWatchosSimulatorArm64?.actIfTrue(receiver::withWatchosSimulatorArm64)
        withWatchosDeviceArm64?.actIfTrue(receiver::withWatchosDeviceArm64)
        withTvosArm64?.actIfTrue(receiver::withTvosArm64)
        withTvosX64?.actIfTrue(receiver::withTvosX64)
        withTvosSimulatorArm64?.actIfTrue(receiver::withTvosSimulatorArm64)
        withLinuxX64?.actIfTrue(receiver::withLinuxX64)
        withMingwX64?.actIfTrue(receiver::withMingwX64)
        withMacosX64?.actIfTrue(receiver::withMacosX64)
        withMacosArm64?.actIfTrue(receiver::withMacosArm64)
        withLinuxArm64?.actIfTrue(receiver::withLinuxArm64)
    }
}

@Serializable
internal data class KotlinHierarchyBuilderImpl(
    override val groups: Set<@Serializable(with = GroupKeyTransformingSerializer::class) Group>? = null,
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
) : KotlinHierarchyBuilder<org.jetbrains.kotlin.gradle.plugin.KotlinHierarchyBuilder>
