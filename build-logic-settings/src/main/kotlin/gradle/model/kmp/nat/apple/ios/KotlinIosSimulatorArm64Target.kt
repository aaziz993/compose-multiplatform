package gradle.model.kmp.nat.apple.ios

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
@SerialName("iosSimulatorArm64")
internal data class KotlinIosSimulatorArm64Target(
    override val targetName: String = "iosSimulatorArm64",
    override val compilations: List<@Serializable(with = KotlinNativeCompilationTransformingSerializer::class) KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
    override val testRuns: List<KotlinNativeSimulatorTestRun>? = null,
) : KotlinNativeTargetWithSimulatorTests(), KotlinIosTarget {

    override val isLeaf: Boolean
        get() = true

    context(Project)
    override fun applyTo() {
        create(kotlin::iosSimulatorArm64)

        super.applyTo()
    }
}
