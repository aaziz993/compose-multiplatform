package gradle.model.kmp.nat.apple.watchos

import gradle.kotlin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import gradle.model.kmp.nat.KotlinNativeBinaryContainer
import gradle.model.kmp.nat.KotlinNativeCompilation
import gradle.model.kmp.nat.KotlinNativeCompilationTransformingSerializer
import gradle.model.kmp.nat.KotlinNativeCompilerOptions
import gradle.model.kmp.nat.KotlinNativeSimulatorTestRun
import gradle.model.kmp.nat.KotlinNativeTargetWithSimulatorTests

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
