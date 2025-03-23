package gradle.plugins.kmp.nat

import gradle.plugins.kmp.nat.tasks.KotlinNativeCompilerOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal abstract class KotlinNativeTargetWithHostTests : KotlinNativeTargetWithTests<KotlinNativeHostTestRun>()

@Serializable
@SerialName("nativeWithHostTests")
internal data class KotlinNativeTargetWithSimulatorTestsImpl(
    override val compilations: List<KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
    override val testRuns: List<@Serializable(with = KotlinNativeHostTestRunTransformingSerializer::class) KotlinNativeHostTestRun>? = null,
) : KotlinNativeTargetWithHostTests() {

    override val targetName: String
        get() = ""
}
