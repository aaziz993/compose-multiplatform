package plugin.project.kmp.model.nat.apple.tvos

import gradle.kotlin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import plugin.project.kmp.model.nat.KotlinNativeBinaryContainer
import plugin.project.kmp.model.nat.KotlinNativeCompilation
import plugin.project.kmp.model.nat.KotlinNativeCompilationTransformingSerializer
import plugin.project.kmp.model.nat.KotlinNativeCompilerOptions
import plugin.project.kmp.model.nat.KotlinNativeSimulatorTestRun
import plugin.project.kmp.model.nat.KotlinNativeTargetWithSimulatorTests

@Serializable
@SerialName("tvosSimulatorArm64")
internal data class KotlinTvosSimulatorArm64Target(
    override val targetName: String = "tvosSimulatorArm64",
    override val compilations: List<@Serializable(with = KotlinNativeCompilationTransformingSerializer::class) KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
    override val testRuns: List<KotlinNativeSimulatorTestRun>? = null,
) : KotlinNativeTargetWithSimulatorTests(), KotlinTvosTarget {

    override val isLeaf: Boolean
        get() = true

    context(Project)
    override fun applyTo() {
        create(kotlin::tvosSimulatorArm64)

        super.applyTo()
    }
}
