package gradle.plugins.kmp.nat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal abstract class KotlinNativeTargetWithSimulatorTests : KotlinNativeTargetWithTests<KotlinNativeSimulatorTestRun>()

@Serializable
@SerialName("nativeWithSimulatorTests")
internal data class KotlinNativeTargetWithHostTestsImpl(
    override val compilations: List<KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
    override val testRuns: List<@Serializable(with = KotlinNativeSimulatorTestRunTransformingSerializer::class) KotlinNativeSimulatorTestRun>? = null,
) : KotlinNativeTargetWithSimulatorTests() {

    override val targetName: String
        get() = ""
}
