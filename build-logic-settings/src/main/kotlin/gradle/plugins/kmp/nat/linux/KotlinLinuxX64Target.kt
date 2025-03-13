package gradle.plugins.kmp.nat.linux

import gradle.accessors.kotlin
import gradle.plugins.kmp.nat.KotlinNativeBinaryContainer
import gradle.plugins.kmp.nat.KotlinNativeCompilation
import gradle.plugins.kmp.nat.KotlinNativeCompilationTransformingSerializer
import gradle.plugins.kmp.nat.KotlinNativeCompilerOptions
import gradle.plugins.kmp.nat.KotlinNativeHostTestRun
import gradle.plugins.kmp.nat.KotlinNativeHostTestRunTransformingSerializer
import gradle.plugins.kmp.nat.KotlinNativeTargetWithHostTests
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

@Serializable
@SerialName("linuxX64")
internal data class KotlinLinuxX64Target(
    override val targetName: String = "linuxX64",
    override val compilations: List<@Serializable(with = KotlinNativeCompilationTransformingSerializer::class) KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
    override val testRuns: List<@Serializable(with = KotlinNativeHostTestRunTransformingSerializer::class) KotlinNativeHostTestRun>? = null,
) : KotlinNativeTargetWithHostTests(), KotlinLinuxTarget {

    context(Project)
    override fun applyTo()  =
        super.applyTo(kotlin.targets.withType<KotlinNativeTarget>(), kotlin::linuxX64)
}
