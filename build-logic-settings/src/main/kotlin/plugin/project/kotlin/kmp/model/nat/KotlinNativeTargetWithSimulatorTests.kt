package plugin.project.kotlin.kmp.model.nat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal abstract class KotlinNativeTargetWithSimulatorTests : KotlinNativeTargetWithTests<KotlinNativeSimulatorTestRun>()

@Serializable
@SerialName("nativeWithSimulatorTests")
internal data class KotlinNativeTargetWithHostTestsImpl(
    override val compilations: List<KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
    override val testRuns: List<KotlinNativeSimulatorTestRun>? = null,
) : KotlinNativeTargetWithSimulatorTests() {

    override val targetName: String
        get() = ""
}
