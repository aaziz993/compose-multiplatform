package gradle.plugins.kmp.nat

import gradle.plugins.kmp.nat.tasks.KotlinNativeCompilerOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal abstract class KotlinNativeTargetWithSimulatorTests : KotlinNativeTargetWithTests<KotlinNativeSimulatorTestRun>()

@Serializable
@SerialName("nativeWithSimulatorTests")
internal data class KotlinNativeTargetWithHostTestsImpl(
    override val compilations: LinkedHashSet<@Serializable(with = KotlinNativeCompilationTransformingSerializer::class) KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
    override val testRuns: List<@Serializable(with = KotlinNativeSimulatorTestRunTransformingSerializer::class) KotlinNativeSimulatorTestRun>? = null,
) : KotlinNativeTargetWithSimulatorTests() {

    override val targetName: String
        get() = ""
}
