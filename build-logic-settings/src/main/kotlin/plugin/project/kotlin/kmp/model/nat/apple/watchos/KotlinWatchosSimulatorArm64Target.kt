package plugin.project.kotlin.kmp.model.nat.apple.watchos

import gradle.kotlin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import plugin.project.kotlin.kmp.model.nat.KotlinNativeBinaryContainer
import plugin.project.kotlin.kmp.model.nat.KotlinNativeCompilation
import plugin.project.kotlin.kmp.model.nat.KotlinNativeCompilationTransformingSerializer
import plugin.project.kotlin.kmp.model.nat.KotlinNativeCompilerOptions
import plugin.project.kotlin.kmp.model.nat.KotlinNativeSimulatorTestRun
import plugin.project.kotlin.kmp.model.nat.KotlinNativeTargetWithSimulatorTests

@Serializable
@SerialName("watchosSimulatorArm64")
internal data class KotlinWatchosSimulatorArm64Target(
    override val targetName: String = "watchosSimulatorArm64",
    override val compilations: List<@Serializable(with = KotlinNativeCompilationTransformingSerializer::class) KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
    override val testRuns: List<KotlinNativeSimulatorTestRun>? = null,
) : KotlinNativeTargetWithSimulatorTests(), KotlinWatchosTarget {

    override val isLeaf: Boolean
        get() = true

    context(Project)
    override fun applyTo() {
        create(kotlin::watchosSimulatorArm64)

        super.applyTo()
    }
}
