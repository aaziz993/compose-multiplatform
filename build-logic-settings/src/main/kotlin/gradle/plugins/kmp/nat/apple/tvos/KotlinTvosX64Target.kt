package gradle.plugins.kmp.nat.apple.tvos

import gradle.accessors.kotlin
import gradle.plugins.kmp.nat.KotlinNativeBinaryContainer
import gradle.plugins.kmp.nat.KotlinNativeCompilation
import gradle.plugins.kmp.nat.KotlinNativeCompilationTransformingSerializer
import gradle.plugins.kmp.nat.KotlinNativeCompilerOptions
import gradle.plugins.kmp.nat.KotlinNativeSimulatorTestRun
import gradle.plugins.kmp.nat.KotlinNativeSimulatorTestRunTransformingSerializer
import gradle.plugins.kmp.nat.KotlinNativeTargetWithSimulatorTests
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

@Serializable
@SerialName("tvosX64")
internal data class KotlinTvosX64Target(
    override val targetName: String = "tvosX64",
    override val compilations: List<@Serializable(with = KotlinNativeCompilationTransformingSerializer::class) KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
    override val testRuns: List<@Serializable(with = KotlinNativeSimulatorTestRunTransformingSerializer::class) KotlinNativeSimulatorTestRun>? = null,
) : KotlinNativeTargetWithSimulatorTests(), KotlinTvosTarget {

    context(Project)
    override fun applyTo() =
        super.applyTo(kotlin.targets.withType<KotlinNativeTarget>(), kotlin::tvosX64)
}
