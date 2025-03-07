package gradle.model.kotlin.kmp.nat.apple.tvos

import gradle.kotlin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import gradle.model.kotlin.kmp.nat.KotlinNativeBinaryContainer
import gradle.model.kotlin.kmp.nat.KotlinNativeCompilation
import gradle.model.kotlin.kmp.nat.KotlinNativeCompilationTransformingSerializer
import gradle.model.kotlin.kmp.nat.KotlinNativeCompilerOptions
import gradle.model.kotlin.kmp.nat.KotlinNativeSimulatorTestRun
import gradle.model.kotlin.kmp.nat.KotlinNativeTargetWithSimulatorTests

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
